package First;
import First.GeneralTree.Node;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.border.LineBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kihakik
 */
public class NewJFrame extends javax.swing.JFrame implements ActionListener {

public HashMap<Integer,Unit> gameMap;
public boolean AIBlue = false;
public boolean BlueTurn = true;
public int score=0;
    /**
     * Creates new form NewJFrame
     */

    
    //izveido sākuma HashMap
    public HashMap<Integer,Unit> startMap(){
    jLabel1.setText(""+score);
    HashMap<Integer,Unit> map = new HashMap<>();
    
    map.put(24,new Unit("horse",24,4,2,1,true));
    map.put(27,new Unit("horse",27,4,2,1,true));
    map.put(74,new Unit("horse",74,4,2,1,false));
    map.put(77,new Unit("horse",77,4,2,1,false));
    
    map.put(25,new Unit("soldier",25,6,1,1,true));
    map.put(26,new Unit("soldier",26,6,1,1,true));
    map.put(75,new Unit("soldier",75,6,1,1,false));
    map.put(76,new Unit("soldier",76,6,1,1,false));
    
    map.put(15,new Unit("archer",15,2,1,2,true));
    map.put(16,new Unit("archer",16,2,1,2,true));
    map.put(85,new Unit("archer",85,2,1,2,false));
    map.put(86,new Unit("archer",86,2,1,2,false));
    
    return map;
}
    
    
    //Heiristiska funkcija
    public int evaluate(HashMap<Integer,Unit> map,int score){
        int BScore=0;
        int RScore=0;
        Unit temp;
        if(score>=20){
            return Integer.MAX_VALUE;
        }
        else if(score<=-20){
        return Integer.MIN_VALUE;
    }
        for (Map.Entry<Integer, Unit> e : map.entrySet()){
            temp = e.getValue();
            if(temp.getBTeam()){
                BScore+=temp.getHp();
                BScore+=3;
            }
            else{
                RScore+=temp.getHp();
                RScore+=3;
            }
    }

            return BScore-RScore+2*score;

    }
    

    //uzzīmē gājienu balstoties uz HashMap
    public void drawTurn(HashMap<Integer,Unit> map) throws IOException{
           
        Unit temp;
        JButton button;
        ImageIcon icon;
        String imageName;
        gamePanel.removeAll();
        gamePanel.revalidate();
        //izveido 100 pogas spēles laukuma pamatam
        for(int i=1; i<=100;i++){
            button = new JButton();
            if(map.containsKey(i)){
                temp=map.get(i);
                imageName=temp.getType();
                if(temp.getBTeam()){
                    imageName+="Blue.png";
                }
                else{
                    imageName+="Red.png";
                }
                icon = new ImageIcon("src/resources/"+imageName);
                
                button.setIcon(icon);
                button.setText(""+temp.getHp());
                button.setVerticalTextPosition(button.CENTER);
                button.setHorizontalTextPosition(button.CENTER);
                Font font = new Font("Arial", Font.PLAIN, 25);
                button.setFont(font);
            }
            else{
                icon = new ImageIcon("src/resources/grass.png");
                button.setIcon(icon);
            }
            if(i%10>=4&&i%10<=7&&i/10>=3&&i/10<=6){
                button.setBorder( new LineBorder(Color.RED) );
            }
            else{
                button.setBorder( new LineBorder(Color.BLACK) );
            }
            
            button.addActionListener(this);
            gamePanel.add(button);
             button.setActionCommand(""+i);
        }
        gamePanel.revalidate();
        gamePanel.repaint();
        
    }
    //veic gājien
    public HashMap<Integer,Unit> makeTurn(int unit, int tile, HashMap<Integer,Unit> map){
        //uzbrūk
        if(map.containsKey(tile)){
            Unit target=map.get(tile);
            int damage = 2;
            //nosaka vai ir ielenkums
            for(int i = -1;i<=+1;i++){
                for(int j=-10;j<=+10;j+=10){
                    if(map.containsKey(tile+i+j)){
                        if(map.get(tile+i+j).getBTeam()!=target.getBTeam()&&unit!=tile+i+j){
                            damage=4;
                        }
                    }
                }
            }
            target.setHp(target.getHp()-damage);
            if(target.getHp()<=0){
                map.remove(tile);
            }
        }
        //pārvieto unitu
        else{
            map.put(tile,map.get(unit));
            map.remove(unit);
        }
        return map;
    }
    
    //Noklonē Hashmap
    public HashMap<Integer,Unit> deepCopyMap(HashMap<Integer,Unit> originalMap) {
    HashMap<Integer,Unit> newMap = new HashMap<Integer,Unit>();
    for (Map.Entry<Integer,Unit> entry : originalMap.entrySet()) {
        Integer key = entry.getKey();
        Unit value = entry.getValue().clone(); 
        newMap.put(key, value);
    }
    return newMap;
}
    public boolean canMakeTurn(int unit, int tile, HashMap<Integer,Unit> map, boolean localBlueTurn){
        if(map.containsKey(unit)){
            boolean teamUnit = map.get(unit).getBTeam();
            if(teamUnit==localBlueTurn){
                if(unit>=1&&unit<=100&&tile>=1&&tile<=100){
                    if(map.containsKey(tile)){
                        
                        boolean teamTile = map.get(tile).getBTeam();
                        if(teamUnit!=teamTile){
                            int range = map.get(unit).getRange();
                            if(Math.abs((unit-1)%10-(tile-1)%10)<=range&&Math.abs(unit/10-tile/10)<=range){
                                
                                return true;
                            }
                        }
                    }
                    else{
                        int mvmt = map.get(unit).getMvmt();
                        if(Math.abs((unit-1)%10-(tile-1)%10)<=mvmt&&Math.abs(unit/10-tile/10)<=mvmt){
                            
                            return true;
                        }   
                    }
                } 
            }
        }
            
        
        return false;
        
        
    }
    
    //pārbauda vai beidzas spēle
    public boolean gameOver(HashMap<Integer,Unit> map,int localScore){
        
        int bCount=0;
        int rCount=0;
        if(!map.isEmpty()){
        for (Map.Entry<Integer, Unit> e : map.entrySet()){
            if(e.getValue().getBTeam()){
                bCount++;
            }
            else{
                rCount++;
            }
        }
        }
        return (bCount==0||rCount==0||localScore>=20||localScore<=-20);
    }
    
    //izbeidz spēli
    public void endGame(HashMap<Integer,Unit> map){
        //neļauj spēlētējam kustināt kauliņus
        BlueTurn=AIBlue;
        int bCount=0;
        int rCount=0;
        if(!map.isEmpty()){
            for (Map.Entry<Integer, Unit> e : map.entrySet()){
                if(e.getValue().getBTeam()){
                    bCount++;
                }
                else{
                    rCount++;
                }
            }
        }
        if(rCount==0||score>=20){
            showMessageDialog(null, "Zilais uzvar!");
        }
        else{
            showMessageDialog(null, "Sarkanais uzvar!");
        }
    }
    //minimax algoritms
    public Node minimax(Node node,HashMap<Integer,Unit> map, int depth,boolean maxPlayer,int score,int alpha,int beta){
        if (depth==0 || gameOver(map,score)){
            node.eval=evaluate(map,score);
            return node;
        }
        //max gadījumā
        if(maxPlayer){
            int maxEval=Integer.MIN_VALUE;
            int currentEval;
            //saņem visus iespējamos gājienus
            ArrayList<int[]> moves=getMoves(map,maxPlayer);
            for(int i=0;i<moves.size();i++){
                HashMap<Integer,Unit> tempMap = deepCopyMap(map);
                //veic gājienu hashmapā
                map=makeTurn(moves.get(i)[0],moves.get(i)[1],deepCopyMap(map));
                //pievienu gājienu kā bērnu nodam
                (node.child).add(GeneralTree.newNode(moves.get(i)[0],moves.get(i)[1]));
                //izsauc rekursīvi līdz nokļūst līdz max dziļumam
                currentEval=minimax(node.child.get(i),deepCopyMap(map),depth-1,false,score+scoreChange(deepCopyMap(map)),alpha,beta).eval;
                //paiet atpakaļ vienu gājienu, lai varētu cikla sākumā atkal darboties tajā pašā līmenī
                map = deepCopyMap(tempMap);
                if (currentEval > maxEval){
                    maxEval=currentEval;
                }
                //palielina alfu
                if(maxEval>=alpha){
                    alpha=maxEval;
                }
                //ja vērtējums padara soli tikpat labu, kā iepriekšējo labāko, beidz apskatīšanu, jo vērtējums var potenciāli palikt tikai sliktāks
                if(beta<alpha){
                    break;
                }
            }
            node.eval=maxEval;
            return node;     
        }
        //min gadījumā
        else{
            int minEval=Integer.MAX_VALUE;
            int currentEval;
            //saņem visus iespējamos gājienus
            ArrayList<int[]> moves=getMoves(map,maxPlayer);
            for(int i=0;i<moves.size();i++){
                HashMap<Integer,Unit> tempMap = deepCopyMap(map);
                //veic gājienu hashmapā
                map=makeTurn(moves.get(i)[0],moves.get(i)[1],deepCopyMap(map));
                //pievienu gājienu kā bērnu nodam
                (node.child).add(GeneralTree.newNode(moves.get(i)[0],moves.get(i)[1]));
                //izsauc rekursīvi līdz nokļūst līdz max dziļumam
                currentEval=minimax(node.child.get(i),deepCopyMap(map),depth-1,true,score+scoreChange(deepCopyMap(map)),alpha,beta).eval;
                //paiet atpakaļ vienu gājienu, lai varētu cikla sākumā atkal darboties tajā pašā līmenī
                map = deepCopyMap(tempMap);
                if (currentEval <= minEval){
                    minEval=currentEval;
                }
                if(minEval<beta){
                    beta=minEval;
                }
                if(beta<alpha){
                    break;
                }
            }
            node.eval=minEval;
            return node;
        }
    }

    public int[] pickMove(Node tree,boolean isMax){
        Node temp;
        int max=Integer.MIN_VALUE;
        int[] move = new int[2];
        int currentEval;
        for (int i = 0; i < tree.child.size(); i++){
            temp = tree.child.get(i);
            currentEval=temp.eval;
            if(!isMax){
                currentEval=currentEval*(-1);
            }
            if(max<currentEval){
                max=currentEval;
                move[0]=temp.unit;
                move[1]=temp.target;
            }
        }
        return move;
    }
    
    public void AITurn() throws IOException{
        
        GeneralTree tree=new GeneralTree();
        Node root=tree.newNode(0, 0);
        Node koks=minimax(root,deepCopyMap(gameMap),4,AIBlue,score,Integer.MIN_VALUE,Integer.MAX_VALUE);
        
        int [] move = pickMove(koks,AIBlue);
        makeTurn(move[0],move[1],gameMap);
        
        drawTurn(gameMap);
        BlueTurn=!BlueTurn;
        updateScore();
        
        if(gameOver(gameMap,score)){
            endGame(gameMap);
        }
    }
    
    public void updateScore(){
        score+=scoreChange(gameMap);
        if(score<-20){
            score=-20;
        }
        else if(score>20){
            score=20;
        }
        jLabel1.setText(""+score);
    }

    public int scoreChange(HashMap<Integer,Unit> map){
        int scoreC=0;
        int coord;
        for (Map.Entry<Integer, Unit> e : map.entrySet()){
            coord=e.getKey();
            if(coord%10>=4&&coord%10<=7&&coord/10>=3&&coord/10<=6){
                if(e.getValue().getBTeam()){
                    scoreC++;
                }
                else{
                    scoreC--;
                }
            }   
        }
        return scoreC;
    }
    
    public ArrayList<int[]> getMoves(HashMap<Integer,Unit> map,boolean isBlue){
        Unit unit;
        ArrayList<int[]> moves = new ArrayList<int[]>();
        
        for (Map.Entry<Integer, Unit> e : map.entrySet()){
            unit=e.getValue();
                for(int i = -2;i<=2;i++){
                    for(int j=-20;j<=20;j+=10){
                            if(canMakeTurn(e.getKey(),e.getKey()+i+j,map,isBlue)){
                                moves.add(new int[] {e.getKey(),e.getKey()+i+j});
                            }
                        }
                    }
            

            
        }
        return moves;
    }
    public void pTurn(String unit, String tile){
        if(AIBlue!=BlueTurn&&canMakeTurn(parseInt(unit),parseInt(tile),gameMap,BlueTurn)){
            try {
                
                //maina spēlētāju gājienus
                BlueTurn=!BlueTurn;
                gameMap=makeTurn(parseInt(unit),parseInt(tile),gameMap);
                drawTurn(gameMap);
                updateScore();
                if(gameOver(gameMap,score)){
                    endGame(gameMap);
                }
                else{
                    AITurn();
                }
                
                
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public NewJFrame() {
        
        initComponents();

    }
    
    //metode, kas nodrošina iepriekšējā klikšķa atcerēšanos
    public String lastClick = null;
    public void doubleClick(String click){
        if(lastClick!=null){
            if(AIBlue!=BlueTurn){
                pTurn(lastClick,click);
            }
            lastClick=null;
        }
        else{
            lastClick=click;
            
        }
    }
    
    
    
    
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        doubleClick(action);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gamePanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        gamePanel.setPreferredSize(new java.awt.Dimension(700, 700));
        gamePanel.setLayout(new java.awt.GridLayout(10, 10));

        jButton1.setBackground(new java.awt.Color(0, 0, 255));
        jButton1.setText("Spēlēt");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setText("Spēlēt");
        jButton2.setPreferredSize(new java.awt.Dimension(70, 70));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText("NOTEIKUMI:\n1.Figūras:\n\nZirgs-pārvietojas 2 lauciņu rādiusā\nuzbrūk 1 lauciņa rādiusā\nKareivis - pārvietojas 1 lauciņs  rādiusā\nuzbrūk 1 lauciņa  rādiusā\nLokšāvējs - pārvietojas 1 lauciņa  rādiusā\nuzbrūk 2 lauciņuu  rādiusā\n\n2.Uzbrukšna:\n\nBez aplenkuma - atņem 2hp\nAr aplenkumu - atņem 4hp\n(Aplenkšana izpildās, kad uzbrukšanas\n mērķim blakus atrodas cita uzbrucēja\n figūra, kas nav uzbrucējs)\n\n3.Punkti:\n\nKatra gājiena beigās aprēķina punktu\nizmaiņu, ņemot vērā figūru skaitu\nsarkanajos lauciņos. Par katru zilo\npunktus palielina par 1, par katru\nsakrano punktus samazina par 1.\n\n4.Uzvara:\n\nZilais uzvar, ja visas sarkanās figūras ir\nnokautas, vai arī punkti ir 20.\n\nSarkanais uzvar, ja visas zilās figūras ir\nnokautas, vai arī punkti ir -20.\n\n");
        jScrollPane2.setViewportView(jTextArea2);
        jTextArea2.getAccessibleContext().setAccessibleParent(jTextArea2);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(449, 449, 449)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        score=0;
        gameMap = startMap();
        try {
            drawTurn(gameMap);
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        AIBlue=false;
        BlueTurn=true;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        score=0;
        gameMap = startMap();
        try {
            drawTurn(gameMap);
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        AIBlue=true;
        BlueTurn=true;
        try {
            AITurn();
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel gamePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
