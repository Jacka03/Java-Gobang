package gobang;

import java.awt.BasicStroke;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GoBangPanel extends JPanel {
	// ��ǰ���壬һΪ�ڶ�Ϊ�ף���ʼΪ��
	private int currentPlayer = GoBangUtil.BLACK;

	private final int CENTUEN = GoBangUtil.LINE_COUNT / 2; // ������������

	private int x = GoBangUtil.LINE_COUNT / 2; // ��¼�߽��������
	private int y = GoBangUtil.LINE_COUNT / 2; //

	private ChessBean[][] chessBeans = new ChessBean[GoBangUtil.LINE_COUNT][GoBangUtil.LINE_COUNT];

	private int count = 0;

	private boolean isGameOver = true; // ��¼��Ϸ�Ƿ�ʼ��

	private boolean isShowOrder = false; // ��¼�Ƿ�Ҫ��ʾ����

	private boolean mode; // ����ģʽ
	private boolean inter; // �˻�ģʽ

	private boolean guzhi;
	private boolean souSuoShu;

	int level; // �������
	int node; // ÿ��ڵ�

	ChessBean chessBeansForTree;

	private TextArea area; // �ұߵ���ʾ�ı���

	public GoBangPanel() { // ���췽������ʼ��
		this.setPreferredSize(new Dimension(GoBangUtil.PANEL_WIDTH, GoBangUtil.PANEL_HEIGHT));
		this.setBackground(Color.ORANGE); // ���ñ���ɫnew Color(200, 100, 50)
		this.addMouseMotionListener(mouseMotionListener); // ����ƶ��¼�
		this.addMouseListener(mouseListener); // ������¼�

		for (int i = 0; i < GoBangUtil.LINE_COUNT; i++) { // ��ʼ��
			for (int j = 0; j < GoBangUtil.LINE_COUNT; j++) {
				ChessBean chessBean = new ChessBean(i, j, GoBangUtil.EMPUTY, 0);
				chessBeans[i][j] = chessBean;
			}
		}
	}

	public void paintComponent(Graphics g) { // ����
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2)); // �Ӵ���
		drawLine(g2d);// ������
		drawStar(g2d); // ����Ԫ����
		drawTrips(g2d, x, y); // ���ѿ�
		drawNumber(g2d); // �����Ե�����
		drawChess1(g2d);// ��������
		drawOrderNum(g2d); // ��ӡ�����ϵ�����
	}

	// ���ƻ�����
	private void drawLine(Graphics2D g2d) {
		for (int i = 0; i < GoBangUtil.LINE_COUNT; i++) { // ����
			g2d.drawLine(GoBangUtil.OFFSET, GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH,
					GoBangUtil.OFFSET + (15 - 1) * GoBangUtil.CELL_WIDTH,
					GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH);
		}
		for (int i = 0; i < 15; i++) { // ����
			g2d.drawLine(GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH, GoBangUtil.OFFSET,
					GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH,
					GoBangUtil.OFFSET + (15 - 1) * GoBangUtil.CELL_WIDTH);
		}
	}

	// ������Ԫ����
	private void drawStar(Graphics2D g2d) {
		// ��Ԫ
		g2d.fillOval(GoBangUtil.LINE_COUNT / 2 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.LINE_COUNT / 2 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// ���Ͻǵ���
		g2d.fillOval(GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// ���½ǵ���
		g2d.fillOval(GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// ���Ͻǵ���
		g2d.fillOval(
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// ���½ǵ���
		g2d.fillOval(
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
	}

	// ���������Ե�����
	private void drawNumber(Graphics2D g2d) {

		for (int i = GoBangUtil.LINE_COUNT; i > 0; i--) {
			FontMetrics fn = g2d.getFontMetrics();
			int height = fn.getAscent();
			g2d.drawString(16 - i + "", 10, i * GoBangUtil.CELL_WIDTH + height / 2); // �������

			int width = fn.stringWidth(((char) (64 + i)) + "");// �������ĸ
			g2d.drawString(((char) (64 + i)) + "", GoBangUtil.CELL_WIDTH * i - width / 2,
					GoBangUtil.OFFSET + GoBangUtil.LINE_COUNT * GoBangUtil.CELL_WIDTH);
		}

	}

	// ������ʾ��
	private void drawTrips(Graphics2D g2d, int i, int j) {
		i = i * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
		j = j * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
		g2d.setColor(Color.red);
		// ���Ͻ�����
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 4,
				j - GoBangUtil.OFFSET / 2);
		// ���Ͻ�����
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 2,
				j - GoBangUtil.OFFSET / 4);
		// ���½�����
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 4,
				j + GoBangUtil.OFFSET / 2);
		// ���½�����
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 2,
				j + GoBangUtil.OFFSET / 4);
		// ���Ͻ�����
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 4,
				j - GoBangUtil.OFFSET / 2);
		// ���Ͻ�����
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 2,
				j - GoBangUtil.OFFSET / 4);
		// ���Ͻ�����
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 4,
				j + GoBangUtil.OFFSET / 2);
		// ���Ͻ�����
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 2,
				j + GoBangUtil.OFFSET / 4);
		g2d.setColor(Color.black);
	}

	// ��������
	private void drawChess1(Graphics2D g2d) {
		int width = GoBangUtil.OFFSET / 4 * 3; // ���ӵĿ��
		for (ChessBean[] chessBeans2 : chessBeans) {
			for (ChessBean chessBean : chessBeans2) {
				if (chessBean.getPlayer() != 0) {
					if (chessBean.getPlayer() == GoBangUtil.BLACK) { // �������
						g2d.setColor(Color.BLACK);
					} else if (chessBean.getPlayer() == GoBangUtil.WHILE) { // �������
						g2d.setColor(Color.WHITE);
					}
					int a = chessBean.getX() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
					int b = chessBean.getY() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
					g2d.fillOval(a - width / 2, b - width / 2, width, width);
				}
			}
			if (count > 0 && !isShowOrder) {
				g2d.setColor(Color.RED); // ���һ�������м���С��ɫ����
				int a = getMaxNum().getX() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.CELL_WIDTH / 10;
				int b = getMaxNum().getY() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.CELL_WIDTH / 10;
				g2d.fillRect(a, b, GoBangUtil.CELL_WIDTH / 5, GoBangUtil.CELL_WIDTH / 5);
				g2d.setColor(Color.black);
			}
		}
	}

	// ��ӡ����
	private void drawOrderNum(Graphics2D g2d) {
		if (isShowOrder) {
			g2d.setColor(Color.RED); // ���������óɺ�ɫ
			FontMetrics fn = g2d.getFontMetrics();
			for (ChessBean[] chssBean2 : chessBeans) {
				for (ChessBean chssBean : chssBean2) {
					if (chssBean.getOrderNumber() != 0) {
						String str = chssBean.getOrderNumber() + ""; // ������ת�����ַ���
						int width = fn.stringWidth(str); // ��ȡ���ֿ��
						int height = fn.getHeight();
						int x = chssBean.getX() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - width / 2; // ��ȡ���ӵ����м�
						int y = chssBean.getY() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET + height / 4;
						g2d.drawString(chssBean.getOrderNumber() + "", x, y); // �����ִ�ӡ���������м�
					}
				}
			}
		}
		g2d.setColor(Color.BLACK); // ����ɫ��ԭΪ��ɫ
	}

	// �������µ�һ������
	private ChessBean getMaxNum() {
		ChessBean tempBean = null;
		for (ChessBean[] chssBean2 : chessBeans) {
			for (ChessBean chssBean : chssBean2) {
				if (chessBeans != null && chssBean.getOrderNumber() == count) {
					return chssBean;
				}
			}
		}
		return tempBean;
	}

	// ȡ����ʾ�������
	private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {
			int cx = e.getX(); // ��ȡ��굱ǰ����
			int cy = e.getY();
			if (cx >= GoBangUtil.OFFSET && cx <= GoBangUtil.OFFSET + (GoBangUtil.LINE_COUNT - 1) * GoBangUtil.CELL_WIDTH
					&& cy >= GoBangUtil.OFFSET
					&& cy <= GoBangUtil.OFFSET + (GoBangUtil.LINE_COUNT - 1) * GoBangUtil.CELL_WIDTH) { // �ж��Ƿ���������
				x = (cx - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH; // �������ת�����������ߵĽ���
				y = (cy - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH;
				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	};

	// ��ʼ����Ϸ
	public void playNewGame(boolean showNumber, boolean mode, boolean inter, int level, int node, boolean guzhi,
			boolean souSuoShu, TextArea area, boolean robot) {
		// TODO �Զ����ɵķ������
		this.mode = mode;
		this.inter = inter;
		this.level = level;
		this.node = node;
		this.guzhi = guzhi;
		this.souSuoShu = souSuoShu;
		this.area = area;

		area.setText("");

		isGameOver = false;
		count = 0;
		JOptionPane.showMessageDialog(GoBangPanel.this, "��Ϸ�Ѿ���ʼ�ˣ�");
		for (int i = 0; i < GoBangUtil.LINE_COUNT; i++) {
			for (int j = 0; j < GoBangUtil.LINE_COUNT; j++) {
				ChessBean chessBean = new ChessBean(i, j, GoBangUtil.EMPUTY, count);
				chessBeans[i][j] = chessBean;
			}
		}
		if (inter && robot) {
			currentPlayer = GoBangUtil.BLACK;
			count++;
			ChessBean temBean = new ChessBean(CENTUEN, CENTUEN, currentPlayer, count);
			chessBeans[CENTUEN][CENTUEN] = temBean;
			currentPlayer = 3 - currentPlayer;
		}
		shouOrderNumber(showNumber);
		repaint();
	}

	// ����ģʽ���壨һ��һ�ŵĻ���
	public void huiQi() {
		if (isGameOver) {
			JOptionPane.showMessageDialog(GoBangPanel.this, "���ȿ�ʼ����Ϸ��");
		} else {
			if (count > 0) {
				ChessBean tempBean = getMaxNum();
				currentPlayer = tempBean.getPlayer();
				chessBeans[tempBean.getX()][tempBean.getY()].setPlayer(GoBangUtil.EMPUTY); //
				chessBeans[tempBean.getX()][tempBean.getY()].setOrderNumber(0);
				count--;
				repaint();
			} else {
				JOptionPane.showMessageDialog(GoBangPanel.this, "�Ѿ������ӿɻ��ˣ�");
			}
		}
	}
	
	// �˻���ս�еĻ���(һ�λ�����
	public void huiQi2() { 
		if (isGameOver) {
			JOptionPane.showMessageDialog(GoBangPanel.this, "���ȿ�ʼ����Ϸ��");
		} else {
			if (count > 2) {
				for (int i = 0; i < 2; i++) {
					ChessBean tempBean = getMaxNum();
					currentPlayer = tempBean.getPlayer();
					chessBeans[tempBean.getX()][tempBean.getY()].setPlayer(GoBangUtil.EMPUTY); //
					chessBeans[tempBean.getX()][tempBean.getY()].setOrderNumber(0);
					count--;
					System.out.println("����");
					repaint();
				}
			} else {
				JOptionPane.showMessageDialog(GoBangPanel.this, "�㻹û�����أ�");
			}
		}
	}
	
	// ����������ʾ����
	public void shouOrderNumber(boolean showNumber) {
		isShowOrder = showNumber;
		repaint();
	}

	private MouseListener mouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if (isGameOver) {
				JOptionPane.showMessageDialog(GoBangPanel.this, "�뿪ʼ�µ���Ϸ");
				return;
			}

			int cx = e.getX(); // ��ȡ��굱ǰ����
			int cy = e.getY();
			int a = (cx - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH; // �������ת�����������ߵĽ���
			int b = (cy - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH;

			if (e.getButton() == MouseEvent.BUTTON1) { // ������
				if (mode) { // ���ˌ���ģʽ
					if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // �ж��Ƿ���������
						if (chessBeans[a][b].getPlayer() == 0) { // �жϸõ��Ƿ�����
							count++;
							ChessBean chessBean = new ChessBean(a, b, currentPlayer, count);
							currentPlayer = 3 - currentPlayer; // ������һ��Ϊ����
							chessBeans[a][b] = chessBean; // ���浱ǰ���������Ϣ
							if (chessBeans[a][b].getPlayer() != 0)
								System.out.println(
										chessBeans[x][y].getPlayer() == GoBangUtil.BLACK ? "�����������" : "�����������");
							checkWin(a, b, chessBeans[a][b].getPlayer());
						}
					}
				} else if (inter) {// �˙C����,
					if (guzhi) { // ��ֵ����
						if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // �ж��Ƿ���������
							if (chessBeans[a][b].getPlayer() == 0) { // �жϸõ��Ƿ�����
								count++;
								chessBeans[a][b] = new ChessBean(a, b, currentPlayer, count);
								System.out.println("�����������");
								currentPlayer = 3 - currentPlayer;
								checkWin(a, b, chessBeans[a][b].getPlayer());
								repaint();
								if (!checkWin(a, b, chessBeans[a][b].getPlayer())) {
									// �������壬�ҵ���ֵ����
									List<ChessBean> orderList = getSortList(currentPlayer, chessBeans);
									ChessBean bean = orderList.get(0);
									count++;
									a = bean.getX();
									b = bean.getY();
									bean.setPlayer(currentPlayer);
									bean.setOrderNumber(count);
									chessBeans[a][b] = bean;
									System.out.println("���ӣ����ԣ��������");
									currentPlayer = 3 - currentPlayer;
									checkWin(a, b, chessBeans[a][b].getPlayer());
								}
							}
						}
					} else if (souSuoShu) { // ��ֵ������������
						if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // �ж��Ƿ���������
							if (chessBeans[a][b].getPlayer() == 0) { // �жϸõ��Ƿ�������
								count++;
								chessBeans[a][b] = new ChessBean(a, b, currentPlayer, count);
								System.out.println("�����������");
								currentPlayer = 3 - currentPlayer;
								repaint();
								if (!checkWin(a, b, chessBeans[a][b].getPlayer())) {
									// �������壬�ҵ���ֵ����
									ChessBean bean;

//									if (level == 3 && node >= 5 && count < 5) { // ����Ϊ�˼���ǰ�漸���������õ�ʱ��
//										List<ChessBean> orderList = getSortList(currentPlayer, chessBeans);
//										bean = orderList.get(0);
//									} else {
//										getValueByTrees2(0, currentPlayer, chessBeans, -Integer.MAX_VALUE,
//												Integer.MAX_VALUE);
//										bean = chessBeansForTree;
//									}
									getValueByTrees2(0, GoBangUtil.BLACK, chessBeans, -Integer.MAX_VALUE,
											Integer.MAX_VALUE);
									bean = chessBeansForTree;
									count++;
									a = bean.getX();
									b = bean.getY();
									bean.setPlayer(currentPlayer);
									bean.setOrderNumber(count);
									chessBeans[a][b] = bean;
									System.out.println("����(ǿ����)�������");
									// System.out.println(bean);
									currentPlayer = 3 - currentPlayer;
									checkWin(a, b, chessBeans[a][b].getPlayer());
									repaint();
								}
							}
						}
					}
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				// �Ҽ�
				if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // �ж��Ƿ���������
					ChessBean[][] temBeans = GoBangPanel.this.clone(chessBeans);
					int offense = getValue(a, b, currentPlayer, temBeans);
					// ����õ�Զ��ֵļ�ֵ
					int defentse = getValue(a, b, 3 - currentPlayer, temBeans);

					temBeans[a][b].getBuffer().append("��(" + (a + 1) + "," + (b - 1) + ")��" + "����:" + offense + " "
							+ "����:" + defentse + " " + "�ܺ�:" + (defentse + offense) + "\n\n");
					area.append(temBeans[a][b].getBuffer().toString());
					// }

				}
			}
			repaint();
		}
	};

	protected int getValueByTrees2(int d, int player, ChessBean[][] chessBeans2, int alpha, int beta) { //
		ChessBean[][] temBeans = clone(chessBeans2);
		List<ChessBean> orderList = getSortList(player, temBeans);
		if (d == level) {
			// �ﵽ����ָ����ȣ����������ص�ǰ�����С���ȡ���Ĺ�ֵ��ߵĵ㡣
			return orderList.get(0).getSum();
		}
		// ������ǰ���������п����λ�ã�����getSortList��
		for (int i = 0; i < node; i++) {
			ChessBean bean = orderList.get(0);
			int score;
			if (bean.getSum() > Level.ALIVE_4.score) {
				// �ҵ�Ŀ��
				score = bean.getSum();
			} else {
				// ���������ģ�����塣�����������������Ͻ�������
				temBeans[bean.getX()][bean.getY()].setPlayer(player);
				// temBeans[bean.getX()][bean.getY()] = bean;
				score = getValueByTrees2(d + 1, 3 - player, temBeans, alpha, beta);
			}
			if (d % 2 == 0) {
				// �Լ��������ֵ
				if (score > alpha) {
					alpha = score;
					if (d == 0) {
						// ���
						chessBeansForTree = bean;
						// System.out.println(chessBeansForTree);
					}
				}
				if (alpha >= beta) {
					// ��֦
					score = alpha;
					return score;
				}
			} else {
				if (score < beta) {
					beta = score;
				}
				if (alpha >= beta) {
					// ��֦
					score = beta;
					return score;
				}
			}
		}
		return d % 2 == 0 ? alpha : beta;
	}

	protected ChessBean getValueByTrees(int d, int player, ChessBean[][] chessBeans) {
		ChessBean[][] tempBeans = clone(chessBeans);
		List<ChessBean> orderList = getSortList(player, tempBeans);
		if (d == level) {
			return orderList.get(0); // �ﵽ����ָ����ȣ����������ص�ǰ�����С���ȡ���Ĺ�ֵ��ߵĵ㡣
		}
		for (int i = 0; i < orderList.size(); i++) {
			// ������ǰ���������п����λ�ã�����getSortList��
			System.out.println("i" + i);
			ChessBean bean = orderList.get(i);
			if (bean.getSum() > Level.ALIVE_4.score) {
				return bean;
			} else {
				// ���������ģ�����塣�����������������Ͻ�������
				tempBeans[bean.getX()][bean.getY()].setPlayer(player);
				return getValueByTrees(d + 1, 3 - player, tempBeans);
			}
		}
		return null;
	}

	private ChessBean[][] clone(ChessBean[][] chessBeans2) { // ��¡��P����������
		ChessBean[][] temBeans = new ChessBean[GoBangUtil.LINE_COUNT][GoBangUtil.LINE_COUNT];
		for (int i = 0; i < GoBangUtil.LINE_COUNT; i++) {
			for (int j = 0; j < GoBangUtil.LINE_COUNT; j++) {
				temBeans[i][j] = new ChessBean(chessBeans2[i][j].getX(), chessBeans2[i][j].getY(),
						chessBeans2[i][j].getPlayer(), chessBeans2[i][j].getOrderNumber());
			}
		}
		return temBeans;
	}

	// �������δ֪�Ĺ�ֵ��Ȼ������
	private List<ChessBean> getSortList(int player, ChessBean[][] tempBeans) { // <ChessBean>

		List<ChessBean> list = new ArrayList<>();
		for (ChessBean[] chessBeans2 : tempBeans) {
			for (ChessBean chessBean : chessBeans2) {
				// �ҿյ�
				if (chessBean.getPlayer() == 0) {
					// ����õ���Լ��ļ�ֵ
					int offense = getValue(chessBean.getX(), chessBean.getY(), player, tempBeans);
					// ����õ�Զ��ֵļ�ֵ
					int defentse = getValue(chessBean.getX(), chessBean.getY(), 3 - player, tempBeans);
					chessBean.setOffense(offense); // ��ȡ�õ�Ĺ�����ֵ
					chessBean.setDefentse(defentse); // ��ȡ�õ�ķ�����ֵ
					chessBean.setSum(offense + defentse); // ��ȡ�õ���ܼ�ֵ
					list.add(chessBean);
				}
			}
		}
		Collections.sort(list); // �����ܼ�ֵ��������
		return list;
	}

	// �����ĸ���������ͣ���ȡLevel
	private int getValue(int x2, int y2, int player, ChessBean[][] tempBeans) {
		Level level1 = getLevel(x2, y2, Direction.HENG, player, tempBeans);
		Level level2 = getLevel(x2, y2, Direction.SHU, player, tempBeans);
		Level level3 = getLevel(x2, y2, Direction.PIE, player, tempBeans);
		Level level4 = getLevel(x2, y2, Direction.NA, player, tempBeans);
		return levelScore(level1, level2, level3, level4) + position[x2][y2];
	}

	// �������͵ķ�ֵ
	private int levelScore(Level level1, Level level2, Level level3, Level level4) {
		int[] levelCount = new int[Level.values().length];
		for (int i = 0; i < Level.values().length; i++) {
			levelCount[i] = 0;
		}
		// ͳ��ĳһ�����ͳ��ֵĴ���
		levelCount[level1.index]++;
		levelCount[level2.index]++;
		levelCount[level3.index]++;
		levelCount[level4.index]++;

		int score = 0;
		if (levelCount[Level.GO_4.index] >= 2
				|| levelCount[Level.GO_4.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// ˫��4����4����
			score = 10000;
		else if (levelCount[Level.ALIVE_3.index] >= 2)// ˫��3
			score = 5000;
		else if (levelCount[Level.SLEEP_3.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// ��3��3
			score = 1000;
		else if (levelCount[Level.ALIVE_2.index] >= 2)// ˫��2
			score = 100;
		else if (levelCount[Level.SLEEP_2.index] >= 1 && levelCount[Level.ALIVE_2.index] >= 1)// ��2��2
			score = 10;
		score = Math.max(score, Math.max(Math.max(level1.score, level2.score), Math.max(level3.score, level4.score)));
		return score;
	}

	private Level getLevel(int x2, int y2, Direction direction, int player, ChessBean[][] tempBeans) {
		String leftString = "";
		String rightString = "";

		String str;

		if (direction == Direction.HENG) {
			leftString = getStringSeq(x2, y2, -1, 0, player, tempBeans);
			rightString = getStringSeq(x2, y2, 1, 0, player, tempBeans);
		} else if (direction == Direction.SHU) {
			leftString = getStringSeq(x2, y2, 0, -1, player, tempBeans);
			rightString = getStringSeq(x2, y2, 0, 1, player, tempBeans);
		} else if (direction == Direction.PIE) {
			leftString = getStringSeq(x2, y2, -1, 1, player, tempBeans);
			rightString = getStringSeq(x2, y2, 1, -1, player, tempBeans);
		} else if (direction == Direction.NA) {
			leftString = getStringSeq(x2, y2, -1, -1, player, tempBeans);
			rightString = getStringSeq(x2, y2, 1, 1, player, tempBeans);
		}

		str = leftString + player + rightString;

		tempBeans[x2][y2].getBuffer().append("(" + (x2 + 1) + "," + (y2 - 1) + ")" + direction + "\t" + str + "\t");

		// ��ȡ���͵ĵ����ַ���
		String rstr = new StringBuilder(str).reverse().toString();
		// ����str��rstrȥLevel���������ƥ��
		for (Level level : Level.values()) {
			Pattern pat = Pattern.compile(level.regex[player - 1]);
			Matcher mat = pat.matcher(str); // ��
			// �����true��ƥ��ɹ�
			boolean r1 = mat.find();
			mat = pat.matcher(rstr); // ��
			// �����true��ƥ��ɹ�
			boolean r2 = mat.find();
			if (r1 || r2) {
				tempBeans[x2][y2].getBuffer().append(level.name + "\n");
				if (direction == Direction.NA) {
					tempBeans[x2][y2].getBuffer().append("\n");
				}
				return level;
			}
		}
		return Level.NULL;
	}

	// ��(x,y)��������8��������ߣ�4������ԭ�С�4���ַ�����β����
	private String getStringSeq(int x2, int y2, int i, int j, int player, ChessBean[][] tempBeans) {
		String sum = "";
		boolean isRight = false;
		if (i < 0 || (i == 0 && j < 0)) {
			isRight = true;
		}
		for (int k = 0; k < 5; k++) {
			x2 += i;
			y2 += j;
			if (x2 > 0 && x2 < GoBangUtil.LINE_COUNT && y2 > 0 && y2 < GoBangUtil.LINE_COUNT) {
				if (isRight) {
					sum = tempBeans[x2][y2].getPlayer() + sum;
				} else {
					sum = sum + tempBeans[x2][y2].getPlayer();
				}
			}
		}
		return sum;
	}

	// ����Ƿ�����������
	protected boolean checkWin(int x2, int y2, int player) {
		boolean win = false;
		if (check(x2, y2, 1, 0, player) + check(x2, y2, -1, 0, player) >= 4) {// �����Ƿ�����������
			win = true;
		} else if (check(x2, y2, 0, 1, player) + check(x2, y2, 0, -1, player) >= 4) {// ������Ƿ�����������
			win = true;
		} else if (check(x2, y2, 1, 1, player) + check(x2, y2, -1, -1, player) >= 4) {// �����Ƿ�����������
			win = true;
		} else if (check(x2, y2, -1, 1, player) + check(x2, y2, 1, -1, player) >= 4) {// �����Ƿ�����������
			win = true;
		}
		if (win) {
			// if(player==1)
			JOptionPane.showMessageDialog(GoBangPanel.this, "��Ϸ�Ѿ�����");
			isGameOver = true;
			return true;
		}
		return false;
	}

	private int check(int x2, int y2, int i, int j, int player) {
		// ��ĳ������i, j�����4�����ӡ�
		int sum = 0;
		for (int k = 0; k < 4; k++) {
			x2 += i;
			y2 += j;
			if (x2 >= 0 && x2 < GoBangUtil.LINE_COUNT && y2 >= 0 && y2 < GoBangUtil.LINE_COUNT) {
				if (chessBeans[x2][y2].getPlayer() == player) {
					sum++;
				} else {
					break;
				}
			}
		}
		return sum;
	}

	// ������Ϣ
	public static enum Level {
		CON_5("����", 0, new String[] { "11111", "22222" }, 100000),
		ALIVE_4("����", 1, new String[] { "011110", "022220" }, 10000),
		GO_4("����", 2, new String[] { "011112|0101110|0110110", "022221|0202220|0220220" }, 500),
		DEAD_4("����", 3, new String[] { "211112", "122221" }, -5),
		ALIVE_3("����", 4, new String[] { "01110|010110", "02220|020220" }, 200),
		SLEEP_3("����", 5,
				new String[] { "001112|010112|011012|10011|10101|2011102", "002221|020221|022021|20022|20202|1022201" },
				50),
		DEAD_3("����", 6, new String[] { "21112", "12221" }, -5),
		ALIVE_2("���", 7, new String[] { "00110|01010|010010", "00220|02020|020020" }, 5),
		SLEEP_2("�߶�", 8,
				new String[] { "000112|001012|010012|10001|2010102|2011002",
						"000221|002021|020021|20002|1020201|1022001" },
				3),
		DEAD_2("����", 9, new String[] { "2112", "1221" }, -5), NULL("null", 10, new String[] { "", "" }, 0);
		private String name;
		private int index;
		private String[] regex;// ������ʽ
		int score;// ��ֵ

		// ���췽��
		private Level(String name, int index, String[] regex, int score) {
			this.name = name;
			this.index = index;
			this.regex = regex;
			this.score = score;
		}

		// ���Ƿ���
		@Override
		public String toString() {
			return this.name;
		}
	};

	// ����
	private static enum Direction {
		HENG, SHU, PIE, NA
	};

	// λ�÷�
	private static int[][] position = { 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
			{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
			{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	

}
