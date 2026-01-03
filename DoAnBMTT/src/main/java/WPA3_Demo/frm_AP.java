package WPA3_Demo;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class frm_AP extends javax.swing.JFrame {

    DefaultTableModel model;
    ServerSocket server;
    SAE sae;
    int seq = 1;
    Thread listenThread;
    boolean apRunning = false;
    HashMap<String, Integer> failCount = new HashMap<>();
    HashMap<String, Long> blockUntil = new HashMap<>();
    HashMap<String, Long> lastAttempt = new HashMap<>();

    public frm_AP() {
        initComponents();
        model = new DefaultTableModel(new String[]{"Seq", "Direction", "Type", "Details"}, 0);
        tblFrame.setModel(model);
        lblStatus.setText("AP Stopped");
        lblStatus.setForeground(Color.RED);
    }
    // ======================== START / STOP AP ========================

    void startAP() {
        try {
            if (apRunning) {
                return;
            }
            server = new ServerSocket(8888);
            apRunning = true;
            lbl("AP Running", Color.GREEN);

            listenThread = new Thread(() -> {
                try {
                    while (apRunning) {
                        Socket cli = server.accept();
                        new Thread(() -> handleClient(cli)).start();
                    }
                } catch (Exception e) {
                    if (apRunning) {
                        e.printStackTrace();
                    }
                }
            });
            listenThread.start();
        } catch (Exception e) {
            lbl("Failed to start AP", Color.RED);
            e.printStackTrace();
        }
    }

    void stopAP() {
        try {
            apRunning = false;
            if (server != null) {
                server.close();
            }
            lbl("AP Stopped", Color.RED);
            txtScalar.setText("");
            txtElement.setText("");
            txtShared.setText("");
            txtPTK.setText("");
            if (sae != null) {
                sae.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================== HANDLE CLIENT ========================
    void handleClient(Socket cli) {
        try {
            DataInputStream in = new DataInputStream(cli.getInputStream());
            DataOutputStream out = new DataOutputStream(cli.getOutputStream());

            sae = new SAE(txtSSID.getText(), txtPass.getText());
            log("STA→AP", "CONNECT", "Client connect");

            while (apRunning && !cli.isClosed()) {
                String m = in.readUTF();
//              String ip = cli.getInetAddress().getHostAddress();
                String ip = cli.getInetAddress().getHostAddress();
                long now = System.currentTimeMillis();

//                long last = lastAttempt.getOrDefault(ip, 0L);
//                if (now - last < 1500) { // thử nhanh hơn 1.5 giây
//                    blockUntil.put(ip, now + 30000); // block 30 giây
//                    out.writeUTF("BLOCKED");
//                    log("AP→ATTACK", "ANTI-CLOG", "Rate limit block " + ip);
//                    cli.close();
//                    return;
//                }
//                lastAttempt.put(ip, now);
                if (blockUntil.containsKey(ip) && System.currentTimeMillis() < blockUntil.get(ip)) {
                    out.writeUTF("BLOCKED");
                    log("AP→STA", "CTRL", "BLOCKED " + ip);
                    cli.close();
                    return;
                }

                if (m.startsWith("COMMIT:")) {
                    String[] p = m.substring(7).split("\\|");
                    sae.scalar = new BigInteger(p[0]);
                    sae.element = new BigInteger(p[1]);

                    BigInteger myElement = sae.commit();
                    BigInteger ss = sae.compute(sae.scalar);

                    SwingUtilities.invokeLater(() -> {
                        txtScalar.setText(sae.scalar.toString());
                        txtElement.setText(myElement.toString());
                        txtShared.setText(ss.toString());
                        byte[] pmk = Crypto.hmac(ss.toByteArray(), (txtSSID.getText() + txtPass.getText()).getBytes());
                        byte[] ptk = Crypto.hmac(pmk, "SAE-PTK".getBytes());
                        txtPTK.setText(Crypto.hex(ptk));
                    });

                    out.writeUTF("COMMIT:" + sae.scalar + "|" + myElement);
                    log("AP→STA", "COMMIT", "scalar=" + sae.scalar + ", element=" + myElement);
                }

                if (m.startsWith("CONFIRM:")) {
                    String hmac = m.substring(8);
                    BigInteger ss = sae.compute(sae.scalar);
                    byte[] pmk = Crypto.hmac(ss.toByteArray(), (txtSSID.getText() + txtPass.getText()).getBytes());
                    byte[] ptk = Crypto.hmac(pmk, "SAE-PTK".getBytes());

                    if (hmac.equals(Crypto.hex(ptk))) {
                        out.writeUTF("ACCEPT");
                        log("AP→STA", "CONFIRM", "ACCEPT");
                        //    lbl("Connected WPA3-SAE", Color.GREEN);
                        failCount.remove(ip);
                        blockUntil.remove(ip);
                    } else {
                        out.writeUTF("REJECT");
                        log("AP→STA", "CONFIRM", "REJECT");
                        //   lbl("Wrong Password", Color.RED);
                        int f = failCount.getOrDefault(ip, 0) + 1;
                        failCount.put(ip, f);

                        if (f == 3) {
                            blockUntil.put(ip, System.currentTimeMillis() + 10_000);
                        }
                        if (f == 5) {
                            blockUntil.put(ip, System.currentTimeMillis() + 60_000);
                        }
                        if (f >= 8) {
                            blockUntil.put(ip, System.currentTimeMillis() + 300_000);
                        }
                    }
                }

                if (m.equals("DISCONNECT")) {
                    log("STA→AP", "CTRL", "DISCONNECT");
                    sae.reset();
                    cli.close();
                    //   lbl("Client Disconnected", Color.RED);
                    break;
                }
            }

        } catch (Exception e) {
            //lbl("Client Disconnected", Color.RED);
        }
    }

    // ======================== LOG & LABEL ========================
    void log(String dir, String type, String detail) {
        model.addRow(new Object[]{seq++, dir, type, detail});
    }

    void lbl(String text, Color c) {
        SwingUtilities.invokeLater(() -> {
            lblStatus.setText(text);
            lblStatus.setForeground(c);
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txt_IP_client6 = new javax.swing.JTextField();
        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        txtShared = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPTK = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSSID = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtScalar = new javax.swing.JTextField();
        txtPass = new javax.swing.JTextField();
        txtElement = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblFrame = new javax.swing.JTable();
        btnStartserver = new javax.swing.JButton();
        btnStopserver = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        txt_IP_client6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IP_client6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("WPA3_Demo AP");

        jLabel4.setText("AP Scalar:");

        jLabel5.setText("AP Element:");

        jLabel8.setText("Status:");

        lblStatus.setForeground(new java.awt.Color(102, 255, 102));
        lblStatus.setText("Running");

        jLabel6.setText("Shared Secret:");

        jLabel7.setText("Derived key(PTK):");

        jLabel3.setText("SSID:");

        jLabel10.setText("Password:");

        tblFrame.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblFrame);

        btnStartserver.setText("Start");
        btnStartserver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartserverActionPerformed(evt);
            }
        });

        btnStopserver.setText("Stop");
        btnStopserver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopserverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStopserver)
                    .addComponent(btnStartserver))
                .addGap(19, 19, 19))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSSID)
                                .addComponent(txtPass))
                            .addGap(96, 96, 96))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(23, 23, 23)
                            .addComponent(txtShared))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(23, 23, 23)
                            .addComponent(txtElement))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtScalar)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 116, Short.MAX_VALUE))))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtPTK)
                            .addGap(2, 2, 2)))
                    .addGap(22, 22, 22)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(24, 24, 24)
                .addComponent(btnStartserver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStopserver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtSSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(lblStatus))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txtScalar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addComponent(jLabel5))
                        .addComponent(txtElement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtShared, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(21, 21, 21)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtPTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(161, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_IP_client6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IP_client6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_IP_client6ActionPerformed

    private void btnStartserverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartserverActionPerformed
        startAP();
    }//GEN-LAST:event_btnStartserverActionPerformed

    private void btnStopserverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopserverActionPerformed
        // TODO add your handling code here:
        stopAP();
    }//GEN-LAST:event_btnStopserverActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_AP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStartserver;
    private javax.swing.JButton btnStopserver;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblFrame;
    private javax.swing.JTextField txtElement;
    private javax.swing.JTextField txtPTK;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtSSID;
    private javax.swing.JTextField txtScalar;
    private javax.swing.JTextField txtShared;
    private javax.swing.JTextField txt_IP_client6;
    // End of variables declaration//GEN-END:variables
}
