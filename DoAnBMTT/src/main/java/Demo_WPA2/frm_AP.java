package Demo_WPA2;

import java.util.Arrays;

public class frm_AP extends javax.swing.JFrame {

    byte[] PMK, ANonce, PTK;
    public boolean isRunning = false;
    java.util.List<frm_Client> clients = new java.util.ArrayList<>();

    
    public frm_AP() {
        initComponents();
        btnStartServer.addActionListener(e -> startAP());
        btnStopServer.addActionListener(e -> stopAP());
    }

    void startAP() {
        try {
            String ssid = txtSSID.getText();
            String pass = txtPass.getText();

            if(ssid.isEmpty() || pass.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập SSID và Password!");
                return;
            }
            PMK = Crypto.pbkdf2(pass, ssid);
            txtPMK.setText(Crypto.hex(PMK));

            ANonce = Crypto.random(32);
            txtANonce.setText(Crypto.hex(ANonce));
            
            isRunning = true;
            lbStatus.setText("Running");
            lbStatus.setForeground(java.awt.Color.GREEN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   public boolean receiveHandshake(frm_Client client,String sHex, String micHex){
    try{
        byte[] snonce = hexToBytes(sHex);
        byte[] mic = hexToBytes(micHex);

        byte[] ptkCheck = WPA2Engine.genPTK(PMK, ANonce, snonce);
        byte[] check = Crypto.hmac(ptkCheck, snonce);

        if(Arrays.equals(mic, check)){
            txtSNonce.setText(sHex);
            txtPTK.setText(Crypto.hex(ptkCheck));
            txtMIC.setText(Crypto.hex(mic));
           // lbStatus.setText("CONNECTED");
           // lbStatus.setForeground(java.awt.Color.GREEN);
           if(!clients.contains(client))
            clients.add(client);
            return true;        // <-- xác thực thành công
            
        }else{
            //lbStatus.setText("REJECTED (Wrong Password/SSID)");
            //lbStatus.setForeground(java.awt.Color.RED);
            return false;       // <-- xác thực thất bại
        }
    }catch(Exception e){ e.printStackTrace(); }
    return false;
}



    void stopAP() {
        isRunning = false;
        lbStatus.setText("Stopped");
        lbStatus.setForeground(java.awt.Color.RED);
        for(frm_Client c : clients){
        c.forceDisconnect();
    }
    clients.clear();
    }

    byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSNonce = new javax.swing.JTextField();
        txtPTK = new javax.swing.JTextField();
        txtSSID = new javax.swing.JTextField();
        txtPass = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        txtPMK = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtANonce = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnStartServer = new javax.swing.JButton();
        btnStopServer = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtMIC = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel8.setText("SNonce:");

        jLabel9.setText("PTK:");

        jLabel2.setText("SSID:");

        jLabel3.setText("Password:");

        jLabel4.setText("Status:");

        jLabel6.setText("PMK:");

        lbStatus.setForeground(new java.awt.Color(255, 51, 51));
        lbStatus.setText("Stopped");

        jLabel7.setText("ANonce:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Demo WPA2 AP");

        btnStartServer.setText("StartServer");

        btnStopServer.setText("StopServer");

        jLabel5.setText("MIC:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addComponent(txtSSID, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnStartServer)
                                    .addComponent(btnStopServer)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtSNonce, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                                    .addComponent(txtANonce, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPMK, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPTK))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtMIC)))
                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnStartServer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnStopServer)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtPMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(lbStatus)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtANonce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSNonce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtPTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtMIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_AP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStartServer;
    private javax.swing.JButton btnStopServer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JTextField txtANonce;
    private javax.swing.JTextField txtMIC;
    private javax.swing.JTextField txtPMK;
    private javax.swing.JTextField txtPTK;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtSNonce;
    private javax.swing.JTextField txtSSID;
    // End of variables declaration//GEN-END:variables
}
