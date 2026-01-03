package WPA3_Demo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class frm_Attack extends javax.swing.JFrame {

    DefaultTableModel model = new DefaultTableModel(
            new String[]{"#", "Password", "Result"}, 0);

    JTextField txtSSID = new JTextField("MyNetwork");
    JTextField txtDelay = new JTextField("300");
    JButton btnStart = new JButton("Start Attack");
    JTable table = new JTable(model);
    JLabel lblStatus = new JLabel("Idle");

    String[] dictionary = {
        "123456", "admin", "qwerty", "password",
        "iloveyou", "12345678", "00000000", "letmein",
        "viettel", "fpttelecom"
    };

    public frm_Attack() {
        setTitle("WPA3 Online Brute-force Attack Simulator");
        setSize(480, 400);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridLayout(3, 2));
        top.add(new JLabel("SSID:"));
        top.add(txtSSID);
        top.add(new JLabel("Delay (ms):"));
        top.add(txtDelay);
        top.add(new JLabel("Status:"));
        top.add(lblStatus);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnStart, BorderLayout.SOUTH);

        btnStart.addActionListener(e -> startAttack());
    }

    void startAttack() {
        model.setRowCount(0);
        new Thread(() -> {
            lbl("Attacking...", Color.ORANGE);
            for (int i = 0; i < dictionary.length; i++) {
                String pw = dictionary[i];
                try {
                    Socket s = new Socket("127.0.0.1", 8888);
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    DataInputStream in = new DataInputStream(s.getInputStream());

                    SAE sae = new SAE(txtSSID.getText(), pw);
                    out.writeUTF("COMMIT:" + sae.scalar + "|" + sae.commit());

                    String res = in.readUTF();   // COMMIT / BLOCKED / REJECT

                    if (res.equals("BLOCKED")) {
                        log(i + 1, pw, "❌ BLOCKED by WPA3");
                        lbl("BLOCKED", Color.RED);
                        s.close();
                        break;
                    } else {
                        log(i + 1, pw, "Rejected");
                    }
                    s.close();
                    Thread.sleep(Integer.parseInt(txtDelay.getText()));
                } catch (Exception ex) {
                    log(i + 1, pw, "Error");
                }
            }
            lbl("Done", Color.GREEN);
        }).start();
    }

    void log(int i, String pw, String r) {
        SwingUtilities.invokeLater(() -> model.addRow(new Object[]{i, pw, r}));
    }

    void lbl(String t, Color c) {
        SwingUtilities.invokeLater(() -> {
            lblStatus.setText(t);
            lblStatus.setForeground(c);
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_Attack().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
