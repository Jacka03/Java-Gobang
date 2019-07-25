package gobang;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class GoBangFrame extends JFrame {

	JButton       nudo;        //悔棋
	JButton       newGame;     //新游戏
	JCheckBox     showNumber;  //显示数字
	GoBangPanel   goBangPanel; 
	JRadioButton  renren;      //人人对战
	JRadioButton  renji;       //人机对战
	JRadioButton  guZhi;       //估值函数
	JRadioButton  souSuoShu;   //搜索树
	JPanel        jPanel1;         
	TextArea      textArea;    //右侧多行文本

	JRadioButton  human;   //人类先手
	JRadioButton  robot;   //机器先手

	JComboBox<Integer> jComboBox1;  //搜索深度
	JComboBox<Integer> jComboBox2;  //搜索节点

	public void start() {
		goBangPanel = new GoBangPanel();
		this.add(goBangPanel, BorderLayout.WEST);  // 界面左侧棋盘

		JPanel rightPanel = new JPanel();          //界面右侧的功能区
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		jPanel1 = new JPanel(new BorderLayout());   // 多行文本框
		jPanel1.setBorder(new TitledBorder("在键盘上单击鼠标右键，查看各个估值"));
		textArea = new TextArea();
		textArea.setEditable(false);
		jPanel1.add(textArea);
		rightPanel.add(jPanel1);

		// 模式
		JPanel jPanel2 = new JPanel();      
		jPanel2.setBorder(new TitledBorder("模式"));
		ButtonGroup bg = new ButtonGroup(); // 两个选框互斥
		renren = new JRadioButton("人人对战");
		renji = new JRadioButton("人机对战");
		renren.setSelected(true);           // 默认人人对战
		bg.add(renren);
		bg.add(renji);
		jPanel2.add(renren);
		jPanel2.add(renji);
		rightPanel.add(jPanel2);

		// 智能
		JPanel jPanel3 = new JPanel();      
		jPanel3.setBorder(new TitledBorder("智能"));
		ButtonGroup bg1 = new ButtonGroup(); // 互斥
		guZhi = new JRadioButton("估值函数");
		souSuoShu = new JRadioButton("估值函數+搜索樹");
		guZhi.setSelected(true);
		bg1.add(guZhi);
		bg1.add(souSuoShu);
		jPanel3.add(guZhi);
		jPanel3.add(souSuoShu);
		rightPanel.add(jPanel3);

		// 搜索樹
		JPanel jPanel4 = new JPanel();
		jPanel4.setBorder(new TitledBorder("搜索树"));
		JLabel jLabel1 = new JLabel("搜索深度：");
		jComboBox1 = new JComboBox<Integer>(new Integer[] { 1, 2, 3 });
		JLabel jLabel2 = new JLabel("每层节点：");
		jComboBox2 = new JComboBox<Integer>(new Integer[] { 3, 5, 10 });
		jPanel4.add(jLabel1);
		jPanel4.add(jComboBox1);
		jPanel4.add(jLabel2);
		jPanel4.add(jComboBox2);
		rightPanel.add(jPanel4);

		// 其他
		JPanel jPanel5 = new JPanel();
		nudo = new JButton("悔棋");
		jPanel5.setBorder(new TitledBorder("其他"));
		JLabel jLabel3 = new JLabel("显示落子顺序：");
		showNumber = new JCheckBox();
		jPanel5.add(jLabel3);
		jPanel5.add(showNumber);
		showNumber.addMouseListener(mouseListener);
		nudo.addMouseListener(mouseListener);    // 添加事件
		jPanel5.add(nudo);
		rightPanel.add(jPanel5);
		newGame = new JButton("新游戲");
		rightPanel.add(newGame);
		newGame.addMouseListener(mouseListener);  // 添加事件
		this.add(rightPanel);

		// 人机模式
		JPanel jPanel6 = new JPanel();
		jPanel6.setBorder(new TitledBorder("人机模式"));
		ButtonGroup bg2 = new ButtonGroup(); 
		human = new JRadioButton("人类先手");
		robot = new JRadioButton("机器先手");
		robot.setSelected(true);
		bg2.add(human);
		bg2.add(robot);
		jPanel6.add(human);
		jPanel6.add(robot);
		rightPanel.add(jPanel6);

		this.setTitle("五子棋");    // 标题
		this.setSize(GoBangUtil.GAME_WIDTH, GoBangUtil.GAME_HEIGHT); // 大小
		this.setLocation(450, 150);   // 初始化位置
		this.setResizable(false);     // 不可伸缩
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			Object object = e.getSource();
			if (object == nudo) {  
				if (renren.isSelected()) {
					goBangPanel.huiQi();
				} else if (renji.isSelected()) {
					goBangPanel.huiQi2();
				}
			} else if (object == newGame) {   // 新游戏
				int level = (Integer) jComboBox1.getSelectedItem();
				int node = (Integer) jComboBox2.getSelectedItem();
				goBangPanel.playNewGame(showNumber.isSelected(), renren.isSelected(), renji.isSelected(), level, node,
						guZhi.isSelected(), souSuoShu.isSelected(), textArea, robot.isSelected());
			} else if (object == showNumber) {   // 显示数字
				goBangPanel.shouOrderNumber(showNumber.isSelected());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	};

}
