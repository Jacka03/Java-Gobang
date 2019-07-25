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
	// 当前下棋，一为黑二为白，开始为黑
	private int currentPlayer = GoBangUtil.BLACK;

	private final int CENTUEN = GoBangUtil.LINE_COUNT / 2; // 棋盘中心坐标

	private int x = GoBangUtil.LINE_COUNT / 2; // 记录线交点的坐标
	private int y = GoBangUtil.LINE_COUNT / 2; //

	private ChessBean[][] chessBeans = new ChessBean[GoBangUtil.LINE_COUNT][GoBangUtil.LINE_COUNT];

	private int count = 0;

	private boolean isGameOver = true; // 记录游戏是否开始了

	private boolean isShowOrder = false; // 记录是否要显示数字

	private boolean mode; // 人人模式
	private boolean inter; // 人机模式

	private boolean guzhi;
	private boolean souSuoShu;

	int level; // 搜索深度
	int node; // 每层节点

	ChessBean chessBeansForTree;

	private TextArea area; // 右边的显示文本框

	public GoBangPanel() { // 构造方法，初始化
		this.setPreferredSize(new Dimension(GoBangUtil.PANEL_WIDTH, GoBangUtil.PANEL_HEIGHT));
		this.setBackground(Color.ORANGE); // 设置背景色new Color(200, 100, 50)
		this.addMouseMotionListener(mouseMotionListener); // 鼠标移动事件
		this.addMouseListener(mouseListener); // 鼠标点击事件

		for (int i = 0; i < GoBangUtil.LINE_COUNT; i++) { // 初始化
			for (int j = 0; j < GoBangUtil.LINE_COUNT; j++) {
				ChessBean chessBean = new ChessBean(i, j, GoBangUtil.EMPUTY, 0);
				chessBeans[i][j] = chessBean;
			}
		}
	}

	public void paintComponent(Graphics g) { // 绘制
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2)); // 加粗线
		drawLine(g2d);// 画棋盘
		drawStar(g2d); // 画天元和星
		drawTrips(g2d, x, y); // 提醒框
		drawNumber(g2d); // 棋盘旁的坐标
		drawChess1(g2d);// 绘制棋子
		drawOrderNum(g2d); // 打印棋子上的数字
	}

	// 绘制画棋盘
	private void drawLine(Graphics2D g2d) {
		for (int i = 0; i < GoBangUtil.LINE_COUNT; i++) { // 横线
			g2d.drawLine(GoBangUtil.OFFSET, GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH,
					GoBangUtil.OFFSET + (15 - 1) * GoBangUtil.CELL_WIDTH,
					GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH);
		}
		for (int i = 0; i < 15; i++) { // 竖线
			g2d.drawLine(GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH, GoBangUtil.OFFSET,
					GoBangUtil.OFFSET + i * GoBangUtil.CELL_WIDTH,
					GoBangUtil.OFFSET + (15 - 1) * GoBangUtil.CELL_WIDTH);
		}
	}

	// 绘制天元和星
	private void drawStar(Graphics2D g2d) {
		// 天元
		g2d.fillOval(GoBangUtil.LINE_COUNT / 2 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.LINE_COUNT / 2 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// 左上角的星
		g2d.fillOval(GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// 左下角的星
		g2d.fillOval(GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// 右上角的星
		g2d.fillOval(
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.LINE_COUNT / 4 * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
		// 右下角的星
		g2d.fillOval(
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				(GoBangUtil.LINE_COUNT - GoBangUtil.LINE_COUNT / 4) * GoBangUtil.CELL_WIDTH - GoBangUtil.STAR_WIDTH / 2,
				GoBangUtil.STAR_WIDTH, GoBangUtil.STAR_WIDTH);
	}

	// 绘制棋盘旁的坐标
	private void drawNumber(Graphics2D g2d) {

		for (int i = GoBangUtil.LINE_COUNT; i > 0; i--) {
			FontMetrics fn = g2d.getFontMetrics();
			int height = fn.getAscent();
			g2d.drawString(16 - i + "", 10, i * GoBangUtil.CELL_WIDTH + height / 2); // 左边数字

			int width = fn.stringWidth(((char) (64 + i)) + "");// 下面的字母
			g2d.drawString(((char) (64 + i)) + "", GoBangUtil.CELL_WIDTH * i - width / 2,
					GoBangUtil.OFFSET + GoBangUtil.LINE_COUNT * GoBangUtil.CELL_WIDTH);
		}

	}

	// 绘制提示框
	private void drawTrips(Graphics2D g2d, int i, int j) {
		i = i * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
		j = j * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
		g2d.setColor(Color.red);
		// 左上角向右
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 4,
				j - GoBangUtil.OFFSET / 2);
		// 左上角向下
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 2,
				j - GoBangUtil.OFFSET / 4);
		// 左下角向右
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 4,
				j + GoBangUtil.OFFSET / 2);
		// 左下角向上
		g2d.drawLine(i - GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i - GoBangUtil.OFFSET / 2,
				j + GoBangUtil.OFFSET / 4);
		// 右上角向左
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 4,
				j - GoBangUtil.OFFSET / 2);
		// 右上角向下
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j - GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 2,
				j - GoBangUtil.OFFSET / 4);
		// 右上角向左
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 4,
				j + GoBangUtil.OFFSET / 2);
		// 右上角向下
		g2d.drawLine(i + GoBangUtil.OFFSET / 2, j + GoBangUtil.OFFSET / 2, i + GoBangUtil.OFFSET / 2,
				j + GoBangUtil.OFFSET / 4);
		g2d.setColor(Color.black);
	}

	// 绘制棋子
	private void drawChess1(Graphics2D g2d) {
		int width = GoBangUtil.OFFSET / 4 * 3; // 棋子的宽度
		for (ChessBean[] chessBeans2 : chessBeans) {
			for (ChessBean chessBean : chessBeans2) {
				if (chessBean.getPlayer() != 0) {
					if (chessBean.getPlayer() == GoBangUtil.BLACK) { // 黑棋玩家
						g2d.setColor(Color.BLACK);
					} else if (chessBean.getPlayer() == GoBangUtil.WHILE) { // 白棋玩家
						g2d.setColor(Color.WHITE);
					}
					int a = chessBean.getX() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
					int b = chessBean.getY() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET;
					g2d.fillOval(a - width / 2, b - width / 2, width, width);
				}
			}
			if (count > 0 && !isShowOrder) {
				g2d.setColor(Color.RED); // 最后一个棋子中间有小红色矩阵
				int a = getMaxNum().getX() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.CELL_WIDTH / 10;
				int b = getMaxNum().getY() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - GoBangUtil.CELL_WIDTH / 10;
				g2d.fillRect(a, b, GoBangUtil.CELL_WIDTH / 5, GoBangUtil.CELL_WIDTH / 5);
				g2d.setColor(Color.black);
			}
		}
	}

	// 打印数字
	private void drawOrderNum(Graphics2D g2d) {
		if (isShowOrder) {
			g2d.setColor(Color.RED); // 将数字设置成红色
			FontMetrics fn = g2d.getFontMetrics();
			for (ChessBean[] chssBean2 : chessBeans) {
				for (ChessBean chssBean : chssBean2) {
					if (chssBean.getOrderNumber() != 0) {
						String str = chssBean.getOrderNumber() + ""; // 将数字转换成字符串
						int width = fn.stringWidth(str); // 获取数字宽度
						int height = fn.getHeight();
						int x = chssBean.getX() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET - width / 2; // 获取棋子的正中间
						int y = chssBean.getY() * GoBangUtil.CELL_WIDTH + GoBangUtil.OFFSET + height / 4;
						g2d.drawString(chssBean.getOrderNumber() + "", x, y); // 将数字打印在棋子正中间
					}
				}
			}
		}
		g2d.setColor(Color.BLACK); // 将颜色复原为黑色
	}

	// 获得最后下的一刻棋子
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

	// 取得提示框的坐标
	private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {
			int cx = e.getX(); // 获取鼠标当前坐标
			int cy = e.getY();
			if (cx >= GoBangUtil.OFFSET && cx <= GoBangUtil.OFFSET + (GoBangUtil.LINE_COUNT - 1) * GoBangUtil.CELL_WIDTH
					&& cy >= GoBangUtil.OFFSET
					&& cy <= GoBangUtil.OFFSET + (GoBangUtil.LINE_COUNT - 1) * GoBangUtil.CELL_WIDTH) { // 判断是否在棋盘内
				x = (cx - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH; // 将座标的转换成棋盘内线的交点
				y = (cy - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH;
				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	};

	// 开始新游戏
	public void playNewGame(boolean showNumber, boolean mode, boolean inter, int level, int node, boolean guzhi,
			boolean souSuoShu, TextArea area, boolean robot) {
		// TODO 自动生成的方法存根
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
		JOptionPane.showMessageDialog(GoBangPanel.this, "游戏已经开始了！");
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

	// 人人模式悔棋（一颗一颗的悔棋
	public void huiQi() {
		if (isGameOver) {
			JOptionPane.showMessageDialog(GoBangPanel.this, "请先开始新游戏！");
		} else {
			if (count > 0) {
				ChessBean tempBean = getMaxNum();
				currentPlayer = tempBean.getPlayer();
				chessBeans[tempBean.getX()][tempBean.getY()].setPlayer(GoBangUtil.EMPUTY); //
				chessBeans[tempBean.getX()][tempBean.getY()].setOrderNumber(0);
				count--;
				repaint();
			} else {
				JOptionPane.showMessageDialog(GoBangPanel.this, "已经无棋子可悔了！");
			}
		}
	}
	
	// 人机对战中的悔棋(一次悔两颗
	public void huiQi2() { 
		if (isGameOver) {
			JOptionPane.showMessageDialog(GoBangPanel.this, "请先开始新游戏！");
		} else {
			if (count > 2) {
				for (int i = 0; i < 2; i++) {
					ChessBean tempBean = getMaxNum();
					currentPlayer = tempBean.getPlayer();
					chessBeans[tempBean.getX()][tempBean.getY()].setPlayer(GoBangUtil.EMPUTY); //
					chessBeans[tempBean.getX()][tempBean.getY()].setOrderNumber(0);
					count--;
					System.out.println("悔棋");
					repaint();
				}
			} else {
				JOptionPane.showMessageDialog(GoBangPanel.this, "你还没下棋呢！");
			}
		}
	}
	
	// 在棋子上显示数字
	public void shouOrderNumber(boolean showNumber) {
		isShowOrder = showNumber;
		repaint();
	}

	private MouseListener mouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if (isGameOver) {
				JOptionPane.showMessageDialog(GoBangPanel.this, "请开始新的游戏");
				return;
			}

			int cx = e.getX(); // 获取鼠标当前坐标
			int cy = e.getY();
			int a = (cx - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH; // 将座标的转换成棋盘内线的交点
			int b = (cy - GoBangUtil.OFFSET / 2) / GoBangUtil.CELL_WIDTH;

			if (e.getButton() == MouseEvent.BUTTON1) { // 鼠标左键
				if (mode) { // 人人對戰模式
					if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // 判断是否在棋盘内
						if (chessBeans[a][b].getPlayer() == 0) { // 判断该点是否有棋
							count++;
							ChessBean chessBean = new ChessBean(a, b, currentPlayer, count);
							currentPlayer = 3 - currentPlayer; // 黑棋下一步为白旗
							chessBeans[a][b] = chessBean; // 储存当前玩家棋子信息
							if (chessBeans[a][b].getPlayer() != 0)
								System.out.println(
										chessBeans[x][y].getPlayer() == GoBangUtil.BLACK ? "黑子下棋完毕" : "白子下棋完毕");
							checkWin(a, b, chessBeans[a][b].getPlayer());
						}
					}
				} else if (inter) {// 人機對戰,
					if (guzhi) { // 估值函数
						if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // 判断是否在棋盘内
							if (chessBeans[a][b].getPlayer() == 0) { // 判断该点是否由棋
								count++;
								chessBeans[a][b] = new ChessBean(a, b, currentPlayer, count);
								System.out.println("白子下棋完毕");
								currentPlayer = 3 - currentPlayer;
								checkWin(a, b, chessBeans[a][b].getPlayer());
								repaint();
								if (!checkWin(a, b, chessBeans[a][b].getPlayer())) {
									// 电脑下棋，找到估值最大的
									List<ChessBean> orderList = getSortList(currentPlayer, chessBeans);
									ChessBean bean = orderList.get(0);
									count++;
									a = bean.getX();
									b = bean.getY();
									bean.setPlayer(currentPlayer);
									bean.setOrderNumber(count);
									chessBeans[a][b] = bean;
									System.out.println("黑子（电脑）下棋完毕");
									currentPlayer = 3 - currentPlayer;
									checkWin(a, b, chessBeans[a][b].getPlayer());
								}
							}
						}
					} else if (souSuoShu) { // 估值函数加搜索树
						if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // 判断是否在棋盘内
							if (chessBeans[a][b].getPlayer() == 0) { // 判断该点是否由其子
								count++;
								chessBeans[a][b] = new ChessBean(a, b, currentPlayer, count);
								System.out.println("白子下棋完毕");
								currentPlayer = 3 - currentPlayer;
								repaint();
								if (!checkWin(a, b, chessBeans[a][b].getPlayer())) {
									// 电脑下棋，找到估值最大的
									ChessBean bean;

//									if (level == 3 && node >= 5 && count < 5) { // 纯属为了减少前面几步电脑所用的时间
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
									System.out.println("黑子(强电脑)下棋完毕");
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
				// 右键
				if (a >= 0 && a < GoBangUtil.LINE_COUNT && b >= 0 && b < GoBangUtil.LINE_COUNT) { // 判断是否在棋盘内
					ChessBean[][] temBeans = GoBangPanel.this.clone(chessBeans);
					int offense = getValue(a, b, currentPlayer, temBeans);
					// 计算该点对对手的价值
					int defentse = getValue(a, b, 3 - currentPlayer, temBeans);

					temBeans[a][b].getBuffer().append("点(" + (a + 1) + "," + (b - 1) + ")的" + "攻击:" + offense + " "
							+ "防御:" + defentse + " " + "总和:" + (defentse + offense) + "\n\n");
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
			// 达到搜索指定深度，结束。返回当前步骤中。获取到的估值最高的点。
			return orderList.get(0).getSum();
		}
		// 遍历当前棋盘上所有空余的位置（遍历getSortList）
		for (int i = 0; i < node; i++) {
			ChessBean bean = orderList.get(0);
			int score;
			if (bean.getSum() > Level.ALIVE_4.score) {
				// 找到目标
				score = bean.getSum();
			} else {
				// 这个步骤是模拟下棋。不能再真正的棋盘上进行落子
				temBeans[bean.getX()][bean.getY()].setPlayer(player);
				// temBeans[bean.getX()][bean.getY()] = bean;
				score = getValueByTrees2(d + 1, 3 - player, temBeans, alpha, beta);
			}
			if (d % 2 == 0) {
				// 自己，找最大值
				if (score > alpha) {
					alpha = score;
					if (d == 0) {
						// 结果
						chessBeansForTree = bean;
						// System.out.println(chessBeansForTree);
					}
				}
				if (alpha >= beta) {
					// 剪枝
					score = alpha;
					return score;
				}
			} else {
				if (score < beta) {
					beta = score;
				}
				if (alpha >= beta) {
					// 剪枝
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
			return orderList.get(0); // 达到搜索指定深度，结束。返回当前步骤中。获取到的估值最高的点。
		}
		for (int i = 0; i < orderList.size(); i++) {
			// 遍历当前棋盘上所有空余的位置（遍历getSortList）
			System.out.println("i" + i);
			ChessBean bean = orderList.get(i);
			if (bean.getSum() > Level.ALIVE_4.score) {
				return bean;
			} else {
				// 这个步骤是模拟下棋。不能再真正的棋盘上进行落子
				tempBeans[bean.getX()][bean.getY()].setPlayer(player);
				return getValueByTrees(d + 1, 3 - player, tempBeans);
			}
		}
		return null;
	}

	private ChessBean[][] clone(ChessBean[][] chessBeans2) { // 克隆棋盤上所有棋子
		ChessBean[][] temBeans = new ChessBean[GoBangUtil.LINE_COUNT][GoBangUtil.LINE_COUNT];
		for (int i = 0; i < GoBangUtil.LINE_COUNT; i++) {
			for (int j = 0; j < GoBangUtil.LINE_COUNT; j++) {
				temBeans[i][j] = new ChessBean(chessBeans2[i][j].getX(), chessBeans2[i][j].getY(),
						chessBeans2[i][j].getPlayer(), chessBeans2[i][j].getOrderNumber());
			}
		}
		return temBeans;
	}

	// 计算空余未知的估值，然后排序
	private List<ChessBean> getSortList(int player, ChessBean[][] tempBeans) { // <ChessBean>

		List<ChessBean> list = new ArrayList<>();
		for (ChessBean[] chessBeans2 : tempBeans) {
			for (ChessBean chessBean : chessBeans2) {
				// 找空点
				if (chessBean.getPlayer() == 0) {
					// 计算该点对自己的价值
					int offense = getValue(chessBean.getX(), chessBean.getY(), player, tempBeans);
					// 计算该点对对手的价值
					int defentse = getValue(chessBean.getX(), chessBean.getY(), 3 - player, tempBeans);
					chessBean.setOffense(offense); // 获取该点的攻击价值
					chessBean.setDefentse(defentse); // 获取该点的防御价值
					chessBean.setSum(offense + defentse); // 获取该点的总价值
					list.add(chessBean);
				}
			}
		}
		Collections.sort(list); // 根据总价值降序排序
		return list;
	}

	// 计算四个方向的棋型，获取Level
	private int getValue(int x2, int y2, int player, ChessBean[][] tempBeans) {
		Level level1 = getLevel(x2, y2, Direction.HENG, player, tempBeans);
		Level level2 = getLevel(x2, y2, Direction.SHU, player, tempBeans);
		Level level3 = getLevel(x2, y2, Direction.PIE, player, tempBeans);
		Level level4 = getLevel(x2, y2, Direction.NA, player, tempBeans);
		return levelScore(level1, level2, level3, level4) + position[x2][y2];
	}

	// 计算棋型的分值
	private int levelScore(Level level1, Level level2, Level level3, Level level4) {
		int[] levelCount = new int[Level.values().length];
		for (int i = 0; i < Level.values().length; i++) {
			levelCount[i] = 0;
		}
		// 统计某一个棋型出现的次数
		levelCount[level1.index]++;
		levelCount[level2.index]++;
		levelCount[level3.index]++;
		levelCount[level4.index]++;

		int score = 0;
		if (levelCount[Level.GO_4.index] >= 2
				|| levelCount[Level.GO_4.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 双活4，冲4活三
			score = 10000;
		else if (levelCount[Level.ALIVE_3.index] >= 2)// 双活3
			score = 5000;
		else if (levelCount[Level.SLEEP_3.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 活3眠3
			score = 1000;
		else if (levelCount[Level.ALIVE_2.index] >= 2)// 双活2
			score = 100;
		else if (levelCount[Level.SLEEP_2.index] >= 1 && levelCount[Level.ALIVE_2.index] >= 1)// 活2眠2
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

		// 获取棋型的倒置字符串
		String rstr = new StringBuilder(str).reverse().toString();
		// 根据str和rstr去Level里进行棋型匹配
		for (Level level : Level.values()) {
			Pattern pat = Pattern.compile(level.regex[player - 1]);
			Matcher mat = pat.matcher(str); // 正
			// 如果是true则匹配成功
			boolean r1 = mat.find();
			mat = pat.matcher(rstr); // 反
			// 如果是true则匹配成功
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

	// 从(x,y)出发，有8个方向的线，4个保持原有。4个字符串首尾调换
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

	// 检查是否有五子相连
	protected boolean checkWin(int x2, int y2, int player) {
		boolean win = false;
		if (check(x2, y2, 1, 0, player) + check(x2, y2, -1, 0, player) >= 4) {// 检查横是否有五子相连
			win = true;
		} else if (check(x2, y2, 0, 1, player) + check(x2, y2, 0, -1, player) >= 4) {// 检查竖是否有五子相连
			win = true;
		} else if (check(x2, y2, 1, 1, player) + check(x2, y2, -1, -1, player) >= 4) {// 检查横是否有五子相连
			win = true;
		} else if (check(x2, y2, -1, 1, player) + check(x2, y2, 1, -1, player) >= 4) {// 检查横是否有五子相连
			win = true;
		}
		if (win) {
			// if(player==1)
			JOptionPane.showMessageDialog(GoBangPanel.this, "游戏已经结束");
			isGameOver = true;
			return true;
		}
		return false;
	}

	private int check(int x2, int y2, int i, int j, int player) {
		// 向某个方向（i, j）检查4个棋子。
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

	// 棋型信息
	public static enum Level {
		CON_5("长连", 0, new String[] { "11111", "22222" }, 100000),
		ALIVE_4("活四", 1, new String[] { "011110", "022220" }, 10000),
		GO_4("冲四", 2, new String[] { "011112|0101110|0110110", "022221|0202220|0220220" }, 500),
		DEAD_4("死四", 3, new String[] { "211112", "122221" }, -5),
		ALIVE_3("活三", 4, new String[] { "01110|010110", "02220|020220" }, 200),
		SLEEP_3("眠三", 5,
				new String[] { "001112|010112|011012|10011|10101|2011102", "002221|020221|022021|20022|20202|1022201" },
				50),
		DEAD_3("死三", 6, new String[] { "21112", "12221" }, -5),
		ALIVE_2("活二", 7, new String[] { "00110|01010|010010", "00220|02020|020020" }, 5),
		SLEEP_2("眠二", 8,
				new String[] { "000112|001012|010012|10001|2010102|2011002",
						"000221|002021|020021|20002|1020201|1022001" },
				3),
		DEAD_2("死二", 9, new String[] { "2112", "1221" }, -5), NULL("null", 10, new String[] { "", "" }, 0);
		private String name;
		private int index;
		private String[] regex;// 正则表达式
		int score;// 分值

		// 构造方法
		private Level(String name, int index, String[] regex, int score) {
			this.name = name;
			this.index = index;
			this.regex = regex;
			this.score = score;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.name;
		}
	};

	// 方向
	private static enum Direction {
		HENG, SHU, PIE, NA
	};

	// 位置分
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
