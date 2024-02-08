package Compilador;

/*import java.awt.List;*/
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



/**
 *
 * @author Ricardo Tzun
 */
public class FormAnalizador extends javax.swing.JFrame {
    
    
    File tokens = new File("src/Files/PalabrasReservadas.txt");
    File operadores = new File("src/Files/Operadores.txt");
    File simbolos = new File("src/Files/Simbolos.txt");
    
    int erroresTotales = 0;
    
    Texto txtTokens = new Texto(tokens);
    Texto txtOperadores = new Texto(operadores);
    Texto txtSimbolos = new Texto(simbolos);
    
    ArrayList<String> tokenLinea = txtTokens.getLines();
    ArrayList<String> operadoresLinea = txtOperadores.getLines();
    ArrayList<String> simbolosLinea = txtSimbolos.getLines();
    
    File file = null;
    Texto txtFile = null;
   
    
    

    public FormAnalizador() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaCodigo = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaAnalisis = new javax.swing.JTextArea();
        MostrarBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        AnalizarBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextAreaCodigo.setColumns(20);
        jTextAreaCodigo.setRows(5);
        jScrollPane1.setViewportView(jTextAreaCodigo);

        jTextAreaAnalisis.setColumns(20);
        jTextAreaAnalisis.setRows(5);
        jScrollPane2.setViewportView(jTextAreaAnalisis);

        MostrarBtn.setText("MOSTRAR");
        MostrarBtn.setName(""); // NOI18N
        MostrarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MostrarBtnActionPerformed(evt);
            }
        });

        jButton2.setText("LIMPIAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        AnalizarBtn.setText("ANALIZAR");
        AnalizarBtn.setName(""); // NOI18N
        AnalizarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnalizarBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(MostrarBtn)
                .addGap(18, 18, 18)
                .addComponent(AnalizarBtn)
                .addGap(21, 653, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MostrarBtn)
                    .addComponent(jButton2)
                    .addComponent(AnalizarBtn))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MostrarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MostrarBtnActionPerformed

        jTextAreaAnalisis.setText("");
        String token = jTextAreaCodigo.getText();
        AnalizadorLexico analizador = new AnalizadorLexico();
        
        if (analizador.evaluarString(token) == true){
            jTextAreaAnalisis.append("No es posible analizar el texto");
        } else {
            if (analizador.evaluarRW(token, tokenLinea) == true){
                jTextAreaAnalisis.append("Palabra Reservada encontrada");
            } else if (analizador.evaluarSY(token, simbolosLinea) == true){
                jTextAreaAnalisis.append("Simbolo encontrado");
            } else if (analizador.evaluarID(token) == true ){
                jTextAreaAnalisis.append("Identificador declarado");
            } else {
                jTextAreaAnalisis.append("Identificador mal declarado");
            }
        }
          
        
       
    }//GEN-LAST:event_MostrarBtnActionPerformed
    

        
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTextAreaCodigo.setText("");
        jTextAreaAnalisis.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void AnalizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalizarBtnActionPerformed
        // TODO add your handling code here:
        
        jTextAreaAnalisis.setText("");
        String token = jTextAreaCodigo.getText();
        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
        
        erroresTotales = 0;
        analizadorLexico.resetErrores();
        
        
                
        if (analizadorLexico.evaluarString(token) == true){
            
            try {
                jTextAreaAnalisis.append(analizadorLexico.evaluarLinea(token));
            } catch (IOException ex) {
                Logger.getLogger(FormAnalizador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            erroresTotales += analizadorLexico.erroresTotales();
            jTextAreaAnalisis.append("\n\t----------------------\n");
            jTextAreaAnalisis.append("ANALISIS FINALIZADO\n");
            jTextAreaAnalisis.append("Total de errores: " + erroresTotales + "\n");
        } else {
            jTextAreaAnalisis.append("Para evaluar expresiones pulse el boton Mostrar");
        }
    }//GEN-LAST:event_AnalizarBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormAnalizador().setVisible(true);
            }
        });
        
        
        
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnalizarBtn;
    private javax.swing.JButton MostrarBtn;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextAreaAnalisis;
    private javax.swing.JTextArea jTextAreaCodigo;
    // End of variables declaration//GEN-END:variables
    
    
    
   
}

