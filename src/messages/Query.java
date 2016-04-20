package messages;

public class Query {

	Descriptor queryDesc;
	byte[] node_port; // 2
	byte[] node_ip; // 4

	byte[] speed; // 2
	byte[] searchStr; // ????

	public Query(Descriptor queryDesc, byte[] node_port, byte[] node_ip, byte[] speed, byte[] searchStr) {
		super();
		this.queryDesc = queryDesc;
		this.node_port = node_port;
		this.node_ip = node_ip;
		this.speed = speed;
		this.searchStr = searchStr;
	}

	public byte[] message() {
		int msglen = 22 + 2 + 4 + 2 + this.searchStr.length;

		byte[] msg = new byte[msglen];
		System.arraycopy(this.queryDesc.message(), 0, msg, 0, 22);
		System.arraycopy(this.node_port, 0, msg, 22, 2);
		System.arraycopy(this.node_ip, 0, msg, 24, 4);
		System.arraycopy(this.speed, 0, msg, 28, 2);
		System.arraycopy(this.searchStr, 0, msg, 34, this.searchStr.length);

		return msg;
	}

	public static Query QueryFromMsg(byte[] msg) {
		Descriptor tmp = Descriptor.DescFromMsg(msg);
		byte[] node_p = new byte[2];
		byte[] node_ip = new byte[4];
		byte[] speed = new byte[2];
		byte[] str = new byte[msg.length - 34];

		System.arraycopy(msg, 22, node_p, 0, 2);
		System.arraycopy(msg, 24, node_ip, 0, 4);
		System.arraycopy(msg, 28, speed, 0, 2);
		System.arraycopy(msg, 32, str, 0, msg.length - 34);

		return new Query(tmp, node_p, node_ip, speed, str);
	}

	public Descriptor getQueryDesc() {
		return queryDesc;
	}

	public void setQueryDesc(Descriptor queryDesc) {
		this.queryDesc = queryDesc;
	}

	public byte[] getNode_port() {
		return node_port;
	}

	public void setNode_port(byte[] node_port) {
		this.node_port = node_port;
	}

	public byte[] getNode_ip() {
		return node_ip;
	}

	public void setNode_ip(byte[] node_ip) {
		this.node_ip = node_ip;
	}

	public byte[] getSpeed() {
		return speed;
	}

	public void setSpeed(byte[] speed) {
		this.speed = speed;
	}

	public byte[] getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(byte[] searchStr) {
		this.searchStr = searchStr;
	}

}
