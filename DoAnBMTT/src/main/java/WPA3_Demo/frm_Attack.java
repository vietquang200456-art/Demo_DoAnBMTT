package WPA3_Demo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;


public class frm_Attack extends javax.swing.JFrame {

    DefaultTableModel model = new DefaultTableModel(
            new String[]{"#", "Password", "AP Response"}, 0);

    JTextField txtSSID = new JTextField("MyNetwork");
    JTextField txtDelay = new JTextField("300");
    JButton btnStart = new JButton("Start Attack");
    JTable table = new JTable(model);
    JLabel lblStatus = new JLabel("Idle");

    String[] demoDict = {
        "12345678","password","admin123","qwerty","letmein",
        "wifi12345","123456789","11111111","00000000","secret123"
    };

    public frm_Attack() {
        setTitle("WPA3 Online Attack + Anti-Clogging Demo");
        setSize(540, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel top = new JPanel(new GridLayout(3,2));
        top.add(new JLabel("SSID:"));   top.add(txtSSID);
        top.add(new JLabel("Delay (ms):")); top.add(txtDelay);
        top.add(new JLabel("Status:")); top.add(lblStatus);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnStart, BorderLayout.SOUTH);

        btnStart.addActionListener(e -> new Thread(this::runAttack).start());
    }

    void runAttack() {
        model.setRowCount(0);
        setStatus("Running...", Color.ORANGE);

        for(int i=0;i<demoDict.length;i++){
            String pw = demoDict[i];

            try(Socket s = new Socket("127.0.0.1",8888)){
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                // SAE commit
                SAE sae = new SAE(txtSSID.getText(), pw);
                out.writeUTF("COMMIT|" + sae.scalar + "|" + sae.commit());

                String r1 = in.readUTF();

                // Anti-Clogging activated
                if(r1.startsWith("TOKEN|")){
                    log(i+1,pw,"TOKEN issued (bot challenged)");
                    continue; // bot không giữ token → drop
                }

                // Continue SAE confirm
                if(r1.equals("CONTINUE")){
                    out.writeUTF("CONFIRM|");
                    String r2 = in.readUTF();

                    if(r2.equals("ACCEPT")){
                        log(i+1,pw,"SUCCESS");
                        JOptionPane.showMessageDialog(this,"Password cracked: "+pw);
                        return;
                    }
                    if(r2.startsWith("BLOCK")){
                        log(i+1,pw,r2);
                        long sec = Long.parseLong(r2.split("\\|")[1]);
                        countdown(sec);
                        return;
                    }
                    log(i+1,pw,"REJECT");
                }

                Thread.sleep(Integer.parseInt(txtDelay.getText()));
            }catch(Exception e){
                log(i+1,pw,"ERROR");
            }
        }

        setStatus("Finished", Color.GREEN);
    }

    void countdown(long sec){
        for(long i=sec;i>0;i--){
            setStatus("BLOCKED "+i+"s", Color.RED);
            try{Thread.sleep(1000);}catch(Exception e){}
        }
        setStatus("Ready", Color.GREEN);
    }

    void log(int i,String pw,String r){
        SwingUtilities.invokeLater(() -> model.addRow(new Object[]{i,pw,r}));
    }

    void setStatus(String t, Color c){
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
