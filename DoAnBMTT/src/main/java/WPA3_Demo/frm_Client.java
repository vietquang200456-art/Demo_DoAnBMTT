/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package WPA3_Demo;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class frm_Client extends javax.swing.JFrame {

    SAE sae;
    Socket sock;
    DataInputStream in;
    DataOutputStream out;
    DefaultTableModel model;
    int seq = 1;
    boolean running = false;

    public frm_Client() {
        initComponents();
        model = new DefaultTableModel(
                new String[]{"Seq", "Direction", "Type", "Details"}, 0);
        tblFrame.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSSID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPass = new javax.swing.JTextField();
        btn_connect = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtScalar = new javax.swing.JTextField();
        txtElement = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtShared = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtPTK = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFrame = new javax.swing.JTable();
        btn_disconnect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("WPA3_Demo Client");

        jLabel3.setText("SSID:");

        jLabel2.setText("Password:");

        btn_connect.setText("Connect");
        btn_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_connectActionPerformed(evt);
            }
        });

        jLabel4.setText("Status:");

        lblStatus.setForeground(new java.awt.Color(255, 102, 102));
        lblStatus.setText("Disconnected");

        jLabel6.setText("Client Scalar:");

        jLabel7.setText("Client Element:");

        jLabel8.setText("Shared Secret:");

        jLabel9.setText("Devied Key(PTK):");

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
        jScrollPane1.setViewportView(tblFrame);

        btn_disconnect.setText("Disconnect");
        btn_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_disconnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(21, 21, 21)
                                .addComponent(txtShared))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtElement))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(txtScalar))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPTK))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(txtSSID))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_disconnect)
                                    .addComponent(btn_connect, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 118, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtSSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_connect, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_disconnect))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtScalar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtElement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(txtShared, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_connectActionPerformed
        connect();
    }//GEN-LAST:event_btn_connectActionPerformed

    private void btn_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_disconnectActionPerformed
        try {
            running = false;
            if (out != null) out.writeUTF("DISCONNECT");
            if (sock != null) sock.close();
        } catch (Exception e) {}
        reset();
        lbl("Disconnected", Color.RED);
        log("STA", "CTRL", "DISCONNECT");

        txtScalar.setText("");
        txtElement.setText("");
        txtShared.setText("");
        txtPTK.setText("");

        lblStatus.setText("Disconnected");
        lblStatus.setForeground(Color.RED);

        log("STA", "CTRL", "DISCONNECT");
    }//GEN-LAST:event_btn_disconnectActionPerformed

    private void connect(){
     try {
            seq = 1;
            running = true;
            model.setRowCount(0);

            sock = new Socket("127.0.0.1", 8888);
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());

            out.writeUTF("PING");
            if (!in.readUTF().equals("RUNNING")) {
                lbl("AP not running", Color.RED);
                sock.close();
                return;
            }

            sae = new SAE(txtSSID.getText(), txtPass.getText());

            BigInteger element = sae.commit();

            txtScalar.setText(sae.scalar.toString());
            txtElement.setText(element.toString());

            log("STA→AP", "COMMIT", "element=" + element);
            out.writeUTF("COMMIT:" + element);

            lbl("Authenticating...", Color.ORANGE);
            new Thread(this::receive).start();

        } catch (Exception e) {
            lbl("AP Offline", Color.RED);
        }
    }
    void receive() {
        try {
            while (running) {          // 🔥 chỉ chạy trong session hiện tại
                String m = in.readUTF();

                if (m.startsWith("COMMIT:")) {
                    BigInteger ap = new BigInteger(m.substring(7));
                    BigInteger ss = sae.compute(ap);

                    SwingUtilities.invokeLater(() -> txtShared.setText(ss.toString()));

                    byte[] pmk = Crypto.sha(ss.toByteArray());
                    byte[] ptk = Crypto.hmac(pmk, "4WAY".getBytes());

                    SwingUtilities.invokeLater(() -> txtPTK.setText(Crypto.hex(ptk)));

                    log("AP→STA", "COMMIT", "element=" + ap);
                    out.writeUTF("CONFIRM:" + Crypto.hex(ptk));
                    log("STA→AP", "CONFIRM", "hmac=" + Crypto.hex(ptk));
                }

                if (m.equals("ACCEPT")) {
                    lbl("Connected WPA3-SAE", Color.GREEN);
                    log("AP→STA", "CONFIRM", "ACCEPT");
                }

                if (m.equals("REJECT")) {
                    running = false;            // 🔥 khóa session
                    lbl("Wrong Password!", Color.RED);
                    sock.close();               // 🔥 đá khỏi AP
                }

            }
        } catch (Exception e) {
            running = false;
            lbl("AP stopped", Color.RED);
        }
    }

    void reset() {
        if (sae != null) sae.reset();
        txtScalar.setText("");
        txtElement.setText("");
        txtShared.setText("");
        txtPTK.setText("");
    }
    
    void log(String dir, String type, String detail) {
        model.addRow(new Object[]{seq++, dir, type, detail});
    }

    void lbl(String text, java.awt.Color c) {
        SwingUtilities.invokeLater(() -> {
            lblStatus.setText(text);
            lblStatus.setForeground(c);
        });
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_connect;
    private javax.swing.JButton btn_disconnect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblFrame;
    private javax.swing.JTextField txtElement;
    private javax.swing.JTextField txtPTK;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtSSID;
    private javax.swing.JTextField txtScalar;
    private javax.swing.JTextField txtShared;
    // End of variables declaration//GEN-END:variables
}
