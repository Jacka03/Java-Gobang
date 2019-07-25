package gobang;

public class ChessBean  implements Comparable<ChessBean>{

	private int x; // 棋子横坐标
	private int y;// 棋子纵坐标
	private int orderNumber; // 记录棋子是第几部的
	private int player;  //棋子颜色，1是黑，二是白,0是无棋子
	
	private int offense;  //攻击分
	private int defentse;  //防御分
	private int sum;   //总分
	
	private StringBuffer buffer = new StringBuffer();
	
	@Override
	public String toString() {
		return "ChessBean [x=" + x + ", y=" + y + ", orderNumber=" + orderNumber + ", player=" + player + ", offense="
				+ offense + ", defentse=" + defentse + ", sum=" + sum + "]";
	}

	public int getOffense() {
		return offense;
	}

	public void setOffense(int offense) {
		this.offense = offense;
	}

	public int getDefentse() {
		return defentse;
	}

	public void setDefentse(int defentse) {
		this.defentse = defentse;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	

	public ChessBean(int x, int y, int player, int orderNumber ){
		super();
		this.x = x;
		this.y = y;
		this.orderNumber = orderNumber;
		this.player = player;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int compareTo(ChessBean chessBean) {
		
		if(this.sum>chessBean.sum) {
			return -1;
		}else if(this.sum<chessBean.sum) {
			return 1;
		}else
			return 0;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}
	
	
}
