package com.llk;


import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.sun.javafx.fxml.expression.Expression.add;

/**
 * @Auther: shuaihu.shen@hand-china.com
 * @Date: 2018/10/22 14:30
 * @Description:
 *     1． 新建一个组件（如JButton）。
 * 　　2． 将该组件添加到相应的面板（如JPanel）。
 * 　　3． 注册监听器以监听事件源产生的事件（如通过ActionListener来响应用户点击按钮）。
 * 　　4． 定义处理事件的方法（如在ActionListener中的actionPerformed中定义相应方法）。
 */
public class LLK implements ActionListener {

    static   int MAX_X = 4;     // 高度
    static    int MAX_Y = 4;   // 宽度
    static    int CELLCATEGORY = 15;   //  传入不同的元素类型个人

     JFrame mainFrame;  // 主面板

    JButton gameButton[][] = new JButton[MAX_X+ 2][MAX_Y+ 2]; // 游戏存储按钮数组
    JButton exitButton = new JButton("退出游戏");;  // 两个菜单按钮
    JButton startButton = new JButton("现在开始");;  // 两个菜单按钮
    JButton newlyButton = new JButton("重新开始");;  // 两个菜单按钮
    JButton resetButton = new JButton("重新开始");;  // 两个菜单按钮
    JButton nextButton = new JButton("下一关");;  // 两个菜单按钮
    JButton beforeButton = new JButton("上一关");;  // 两个菜单按钮


     static int[][] grid = new int[MAX_X+2][MAX_Y+2];  // 游戏按钮位置 含边框 6 = 4+1+1

    static   boolean pressFlag = false ; //  按钮是否被选中

      JPanel centerJPanel, menuPanel, downJPanel; // 子面板


    JLabel timeJLabel = new JLabel();   // 时间组件
    JLabel pointJLabel = new JLabel();   // 分数组件


     EvenHandler evenHandler = new EvenHandler()  ;

    /*******************************************
     * 初始化布局
     */
    public void init(){

       mainFrame = new JFrame("LLKD");
        Container container = mainFrame.getContentPane(); // 初始化mainframe
        container.setLayout(new BorderLayout());  // 布置容器的边框布局

         centerJPanel = new JPanel();   // 游戏界面
         downJPanel = new JPanel();
         menuPanel = new JPanel();  // 菜单栏

        pointJLabel = new JLabel("0"); // 定义分数标签，并初始化为0.
        pointJLabel.setText(String.valueOf(Integer.parseInt(pointJLabel.getText())));  // 分数

        menuPanel.add(pointJLabel); // 将“分数”标签加入northPanel

        container.add(centerJPanel,"Center");
        container.add(downJPanel,"South");
        container.add(menuPanel,"East");

        centerJPanel.setLayout(new GridLayout(MAX_X+2,MAX_Y+2)); // MAX_X * MAX_Y 网格布局


        JPanel controlJPanel = new JPanel();  //  控制组件
        controlJPanel.setBackground(new Color(127, 174, 252));
        controlJPanel.setBorder(new EtchedBorder());
        BoxLayout controlLayout = new BoxLayout(controlJPanel, BoxLayout.Y_AXIS);  // Y_AXIS 表示垂直排列
        controlJPanel.setLayout(controlLayout);

        add(controlJPanel, BorderLayout.EAST);

        JPanel pointTextJPanel = new JPanel(); //  得分 文本
        pointTextJPanel.setBackground(new Color(169, 210, 254));
        pointTextJPanel.setBorder(new EtchedBorder());
        JLabel pointTextJLabel = new JLabel();
        pointTextJLabel.setText("得分");
        pointTextJPanel.add(pointTextJLabel);
        controlJPanel.add(pointTextJPanel);

        JPanel pointJPanel = new JPanel();  // 得分内容

        pointJPanel.setBorder(new EtchedBorder());
        pointJPanel.setBackground(new Color(208, 223, 255));

        pointJLabel.setText("0");
        pointJPanel.add(pointJLabel);
        controlJPanel.add(pointJPanel);

        JPanel timeTextPanel = new JPanel();  //  时间 文本
        timeTextPanel.setBackground(new Color(169, 210, 254));
        timeTextPanel.setBorder(new EtchedBorder());
        JLabel timeTextLabel = new JLabel();
        timeTextLabel.setText("剩余时间 (s)");
        timeTextPanel.add(timeTextLabel);
        controlJPanel.add(timeTextPanel);

        // count time
        JPanel timePanel = new JPanel();  //  时间内容
        timePanel.setBorder(new EtchedBorder());
        timePanel.setBackground(new Color(208, 223, 255));

        timeJLabel.setText("0");
        timePanel.add(timeJLabel);
        controlJPanel.add(timePanel);

        JPanel startJPanel = new JPanel();  // 按钮
        startJPanel.setBorder(new EtchedBorder());
        startJPanel.add(startButton);
        startButton.addActionListener(this); // 向“重列”按钮添加事件监听
        controlJPanel.add(startJPanel);

        JPanel resetJPanel = new JPanel();  // 按钮
        resetJPanel.setBorder(new EtchedBorder());
        resetJPanel.setBackground(new Color(208, 223, 255));
        resetJPanel.add(resetButton);
        resetButton.addActionListener(this); // 向“重列”按钮添加事件监听
        controlJPanel.add(resetJPanel);

        JPanel nextJPanel = new JPanel();  // 下一局
        nextJPanel.setBorder(new EtchedBorder());
        nextJPanel.add(nextButton);
        ImageIcon  nextIcon = new ImageIcon("image/next.png");
        nextButton.setIcon(nextIcon);
        nextButton.addActionListener(this); // 向“重列”按钮添加事件监听
        controlJPanel.add(nextJPanel);


        JPanel beforeJPanel = new JPanel();  // 上一局
        beforeJPanel.setBorder(new EtchedBorder());
        beforeJPanel.setBackground(new Color(208, 223, 255));
        beforeJPanel.add(beforeButton);
        ImageIcon  beforeIcon = new ImageIcon("image/left.png");
        beforeButton.setIcon(beforeIcon);
        beforeButton.addActionListener(this); // 向“重列”按钮添加事件监听
        controlJPanel.add(beforeJPanel);



        JPanel exitJPanel = new JPanel();  // 退出
        exitJPanel.setBorder(new EtchedBorder());
        exitJPanel.add(exitButton);
        // 设置按钮背景图像
        ImageIcon  exitIcon = new ImageIcon("image/wrong.png");
        exitButton.setIcon(exitIcon);
        exitButton.addActionListener(this); // 向“重列”按钮添加事件监听
        controlJPanel.add(exitJPanel);




        JPanel initJPanel = new JPanel();
        JLabel initJLabel = new JLabel();
        initJLabel.setText("欢迎来到连连看世界");
        Icon icon=new ImageIcon("image/user7-128x128.jpg");//实例化Icon对象
        initJLabel.setIcon(icon);//为标签设置图片
        initJLabel.setHorizontalAlignment(SwingConstants.CENTER);  //设置文字放置在标签中间
        initJLabel.setOpaque(false);//设置标签为不透明状态
        initJLabel.setSize(128,128);
        initJPanel.add(initJLabel);
        centerJPanel.add(initJPanel);


        // for (int cols = 0; cols < MAX_X + 2; cols++){
        //     for (int rows = 0; rows < MAX_Y + 2 ; rows++){
        //         gameButton[cols][rows] = new JButton(String.valueOf(grid[cols][rows]));   //  新建按钮
        //         gameButton[cols][rows].addActionListener(this); // 添加监听事件
        //         centerJPanel.add(gameButton[cols][rows]);
        //
        //         if (cols == 0 || rows ==0||cols == MAX_X+1 || rows == MAX_Y+1 ){
        //             grid [cols][rows]= 0;
        //             gameButton[cols][rows].setVisible(false);
        //         }
        //     }
        // }


       menuPanel.add(controlJPanel);

            int height = 650;
            int width = 800;
        mainFrame.setBounds((1366-width)/2,(768-height)/2,width,height);     // 显示在屏幕中间
        mainFrame.setVisible(true);  // 可见

      //   new GameTimer(timeJLabel,centerJPanel);
    }


    /******************************************
     * 随机生成数组
     */
    public void randomBuild(){

        int randoms , cols, rows ;
        for (int twins = 1; twins <= (MAX_X*MAX_Y/2); twins++){
            randoms = (int)(Math.random() *  CELLCATEGORY + 1);

            for (int alike = 1; alike <= 2; alike++){  //  将生成的数字填充到格子里面
                cols = (int)(Math.random() *  MAX_X + 1 );
                rows = (int)(Math.random() *  MAX_Y + 1);

                while (grid[cols][rows] != 0){
                    cols = (int)(Math.random() *  MAX_X + 1 );
                    rows = (int)(Math.random() *  MAX_Y + 1 );
                }
                this.grid[cols][rows] = randoms;   // 填充
            }


        }

    }


    public static void main(String[] args) {
        LLK llk = new LLK(); // 初始化
        // llk.randomBuild();
         llk.init(); // 调用init

     }

    public void  newlyGame(){
        int grid[][] = new int[MAX_X+2][MAX_Y+2]; // 产生新的存储按键的数组
        this.grid = grid;
        randomBuild(); // 调用randomBuild
        mainFrame.setVisible(false); // 使当前mainFrame不可见
        pressFlag = false; // 使该 变量还原为false
        init(); // 调用init
    }

    /**
     *  按钮的监控
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == exitButton){
            System.exit(0);
        }

        if (e.getSource() == newlyButton) {
           nextGame(0,0);  // 重新开始
            test( );
        }
        if (e.getSource() == startButton) {
            nextGame(0,0);  // 现在开始
            test( );
        }

        if (e.getSource() == nextButton){
            nextGame(2,5); //  下一局
            test( );
        }

        if (e.getSource() == resetButton){
            nextGame(0,0); //  重玩
            test( );
        }

        if (e.getSource() == beforeButton){  //  上一局
            if (MAX_X < 4){
                JOptionPane.showMessageDialog(null,
                        "当前为第一关，无法选择上一关.", "提示",JOptionPane.PLAIN_MESSAGE);
            }else {
                nextGame(-2,-5);
                test( );
            }

        }


        //  鼠标点击按钮
        for (int x = 0; x < MAX_X+2; x++){
            for (int y= 0 ; y < MAX_Y+2; y++){
                if (e.getSource() == gameButton[x][y]){
                    //  调用 开始判断 进入核心算法
                    Cell cell = new Cell((x),(y),gameButton[x][y]);

                    int result = evenHandler.coreEven(cell,centerJPanel,gameButton,
                             pointJLabel, grid );
                    if (result == 0){
                        //  重玩
                        nextGame(0,0);
                        test();   //  检验死局
                    }
                    if (result == 1){
                        //  下一关
                       nextGame(2,8);
                       test();   //  检验死局
                    }
                }
            }
        }

        mainFrame.setVisible(true);  // 可见
    }


    /**********************************
     *   验证是否出现死局
     */
    public void test( ){


        int[]  gridCopy = new int[(MAX_X+2)*(MAX_Y+2)];

        int n = 0 ;
         for (int i = 0; i < MAX_X+2; i++) {
            for (int j = 0; j < MAX_Y+2; j++) {
                 gridCopy[n] = this.grid[i][j]; // 将现在任然存在的数字存入save中
                n++;
            }
        }


        boolean result = validation();
        if (!result){
            result = validation();
        }
        if (!result){
            result = validation();
        }
        if (result){
            // 没有问题 需要赋值上去
            int m = 0 ;
            for (int i = 0; i < MAX_X+2; i++) {
                for (int j = 0; j < MAX_Y+2; j++) {
                     this.grid[i][j] = gridCopy[m] ;
                    m++;
                }
            }

          //  nextGameButton();

         }else {
           // new Exception(" 死局 ");
            System.out.println("死局");
            nextGame(0,0);
            test( );  // 验证是否死局
        }

    }


    public boolean validation( ) {

        for (int x = 1; x < grid.length -1; x++) {

            for (int y = 1; y < grid[y].length -1; y++) {

                if (gameButton[x][y].getText().equals(0)){
                    continue;
                }
                if (x == grid.length -2 && y == grid[y].length -2 ){  //  最后一个不用便利
                    continue;
                }
                Cell cell1   = new Cell(x, y, gameButton[x][y]);

                boolean   result  =   validation2(x,y,cell1 );

                if (result){

                    return true;  // 成功
                }

            }
         }

        return false;
    }

    public boolean validation2(int x, int y ,Cell cell1 ) {


        int result = -1;
        for (int i = 1; i < grid.length -1; i++) {

            for (int j = 1; j < grid[i].length -1; j++) {
                if (i <  x  ){
                    continue;
                }
                if (i <  x+1  && j <  y ){
                    continue;
                }
                if (i ==1 && j == 1){
                    continue;
                }
                if (gameButton[x][y].getText().equals(0)){
                    continue;
                }
                Cell  cell2   = new Cell(i,j,gameButton[i][j]);
             //    System.out.println ("2、" +cell2.toString());
                // 先判断数值是否相等

                String val1 = cell1.getJButton().getText();
                String val2 = cell2.getJButton().getText();
                 if (cell2.getJButton() != cell1.getJButton()&&val1.equals(val2)){
                      result =  evenHandler.linkedTest(cell1,cell2,centerJPanel,gameButton,grid );
                 //   System.out.println (cell1.toString()+"  "+ cell2.toString());
                }

            }
         }

        if (result == 0){   //  结果为0 时  棋盘为空
            System.out.println ("完全消除完毕,没有问题了！！");
            return true;
        }
        return false;
    }


    /**
     *  格子里面填数  重新装  重置
     */
    public void nextGame(int addNum, int category) {

        MAX_X = MAX_X + addNum ;
        MAX_Y = MAX_Y + addNum;

        CELLCATEGORY = CELLCATEGORY  + category;
        grid =   new int[MAX_X+2][MAX_Y+2];
        gameButton  = new JButton[MAX_X+ 2][MAX_Y+ 2];

        centerJPanel.setLayout(new GridLayout(MAX_X+2,MAX_Y+2)); // MAX_X * MAX_Y 网格布局

        int randoms , cols, rows ;
        List<Integer> icon = new ArrayList< >();
        Set<Integer> iconSet = new TreeSet<>();

        for (int twins = 1; twins <= (MAX_X*MAX_Y/2); twins++){
            randoms = (int)(Math.random() *  CELLCATEGORY + 1);
            iconSet.add(randoms);
            for (int alike = 1; alike <= 2; alike++){  //  将生成的数字填充到格子里面
                cols = (int)(Math.random() *  MAX_X + 1 );
                rows = (int)(Math.random() *  MAX_Y + 1);

                while (grid[cols][rows] != 0){
                    cols = (int)(Math.random() *  MAX_X + 1 );
                    rows = (int)(Math.random() *  MAX_Y + 1 );
                }
                this.grid[cols][rows] = randoms;   // 填充
            }

        }


        icon.addAll(iconSet);
        //按条件过滤
        int[][] icon2 = new int[MAX_X+2][MAX_Y+2];
        for (int i = 0; i < iconSet.size(); i++){

            for (int x = 0; x < MAX_X + 2; x++){
                for (int y = 0; y < MAX_Y + 2 ; y++){

                    if (icon.get(i) == grid[x][y]){
                        icon2[x][y] = i;
                    }
                }
            }

        }

       // List<User> filterList = list.stream().filter(user -> user.getId() > 5 ).collect(Collectors.toList());
        nextGameButton(icon2);

    }

    public void nextGameButton( int[][]  icon) {

        mainFrame.setVisible(false);
        pressFlag = false; // 这里一定要将按钮点击信息归为初始
        centerJPanel.removeAll();
        for (int cols = 0; cols < MAX_X + 2; cols++){
            for (int rows = 0; rows < MAX_Y + 2 ; rows++){

                //  String.valueOf(grid[cols][rows])
                gameButton[cols][rows] = new JButton(String.valueOf(grid[cols][rows]));   //  新建按钮

                // ImageIcon  nextIcon = new ImageIcon("image/icon/default"+icon[cols][rows]+".png");
                // gameButton[cols][rows].setMaximumSize(new Dimension(48,48));//设置按钮和图片的大小相同
                // gameButton[cols][rows].setIcon(nextIcon);
                // gameButton[cols][rows].setFocusPainted(false);
                // gameButton[cols][rows].setContentAreaFilled(false);//设置图片填满按钮所在的区域
                // gameButton[cols][rows].setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离
                // gameButton[cols][rows].setBorderPainted(false); //设置按钮边界不显示
               // gameButton[cols][rows].setHideActionText(false);

                gameButton[cols][rows].addActionListener(this); // 添加监听事件
                centerJPanel.add(gameButton[cols][rows]);

                if (cols == 0 || rows ==0||cols == MAX_X+1 || rows == MAX_Y+1 ){
                    grid [cols][rows]= 0;
                    gameButton[cols][rows].setVisible(false);
                }
            }
        }

       int time = Integer.parseInt(timeJLabel.getText());
        if (time  == 0){
            timeJLabel.setText(String.valueOf(150));
            new GameTimer(timeJLabel,centerJPanel);
        }else {
            timeJLabel.setText(String.valueOf(150));
        }

    }


}