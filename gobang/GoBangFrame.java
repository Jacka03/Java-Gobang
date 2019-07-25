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

	JButton       nudo;        //����
	JButton       newGame;     //����Ϸ
	JCheckBox     showNumber;  //��ʾ����
	GoBangPanel   goBangPanel; 
	JRadioButton  renren;      //���˶�ս
	JRadioButton  renji;       //�˻���ս
	JRadioButton  guZhi;       //��ֵ����
	JRadioButton  souSuoShu;   //������
	JPanel        jPanel1;         
	TextArea      textArea;    //�Ҳ�����ı�

	JRadioButton  human;   //��������
	JRadioButton  robot;   //��������

	JComboBox<Integer> jComboBox1;  //�������
	JComboBox<Integer> jComboBox2;  //�����ڵ�

	public void start() {
		goBangPanel = new GoBangPanel();
		this.add(goBangPanel, BorderLayout.WEST);  // �����������

		JPanel rightPanel = new JPanel();          //�����Ҳ�Ĺ�����
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		jPanel1 = new JPanel(new BorderLayout());   // �����ı���
		jPanel1.setBorder(new TitledBorder("�ڼ����ϵ�������Ҽ����鿴������ֵ"));
		textArea = new TextArea();
		textArea.setEditable(false);
		jPanel1.add(textArea);
		rightPanel.add(jPanel1);

		// ģʽ
		JPanel jPanel2 = new JPanel();      
		jPanel2.setBorder(new TitledBorder("ģʽ"));
		ButtonGroup bg = new ButtonGroup(); // ����ѡ�򻥳�
		renren = new JRadioButton("���˶�ս");
		renji = new JRadioButton("�˻���ս");
		renren.setSelected(true);           // Ĭ�����˶�ս
		bg.add(renren);
		bg.add(renji);
		jPanel2.add(renren);
		jPanel2.add(renji);
		rightPanel.add(jPanel2);

		// ����
		JPanel jPanel3 = new JPanel();      
		jPanel3.setBorder(new TitledBorder("����"));
		ButtonGroup bg1 = new ButtonGroup(); // ����
		guZhi = new JRadioButton("��ֵ����");
		souSuoShu = new JRadioButton("��ֵ����+������");
		guZhi.setSelected(true);
		bg1.add(guZhi);
		bg1.add(souSuoShu);
		jPanel3.add(guZhi);
		jPanel3.add(souSuoShu);
		rightPanel.add(jPanel3);

		// ������
		JPanel jPanel4 = new JPanel();
		jPanel4.setBorder(new TitledBorder("������"));
		JLabel jLabel1 = new JLabel("������ȣ�");
		jComboBox1 = new JComboBox<Integer>(new Integer[] { 1, 2, 3 });
		JLabel jLabel2 = new JLabel("ÿ��ڵ㣺");
		jComboBox2 = new JComboBox<Integer>(new Integer[] { 3, 5, 10 });
		jPanel4.add(jLabel1);
		jPanel4.add(jComboBox1);
		jPanel4.add(jLabel2);
		jPanel4.add(jComboBox2);
		rightPanel.add(jPanel4);

		// ����
		JPanel jPanel5 = new JPanel();
		nudo = new JButton("����");
		jPanel5.setBorder(new TitledBorder("����"));
		JLabel jLabel3 = new JLabel("��ʾ����˳��");
		showNumber = new JCheckBox();
		jPanel5.add(jLabel3);
		jPanel5.add(showNumber);
		showNumber.addMouseListener(mouseListener);
		nudo.addMouseListener(mouseListener);    // ����¼�
		jPanel5.add(nudo);
		rightPanel.add(jPanel5);
		newGame = new JButton("���Α�");
		rightPanel.add(newGame);
		newGame.addMouseListener(mouseListener);  // ����¼�
		this.add(rightPanel);

		// �˻�ģʽ
		JPanel jPanel6 = new JPanel();
		jPanel6.setBorder(new TitledBorder("�˻�ģʽ"));
		ButtonGroup bg2 = new ButtonGroup(); 
		human = new JRadioButton("��������");
		robot = new JRadioButton("��������");
		robot.setSelected(true);
		bg2.add(human);
		bg2.add(robot);
		jPanel6.add(human);
		jPanel6.add(robot);
		rightPanel.add(jPanel6);

		this.setTitle("������");    // ����
		this.setSize(GoBangUtil.GAME_WIDTH, GoBangUtil.GAME_HEIGHT); // ��С
		this.setLocation(450, 150);   // ��ʼ��λ��
		this.setResizable(false);     // ��������
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
			} else if (object == newGame) {   // ����Ϸ
				int level = (Integer) jComboBox1.getSelectedItem();
				int node = (Integer) jComboBox2.getSelectedItem();
				goBangPanel.playNewGame(showNumber.isSelected(), renren.isSelected(), renji.isSelected(), level, node,
						guZhi.isSelected(), souSuoShu.isSelected(), textArea, robot.isSelected());
			} else if (object == showNumber) {   // ��ʾ����
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
