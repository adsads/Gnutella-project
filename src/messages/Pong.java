package messages;

public class Pong {

	public Descriptor pongDesc; // 22 bytes

	public byte[] node_port; // 2 bytes
	public byte[] node_ip; // 4 bytes
	public byte[] file_count; // 4 bytes
	public byte[] total_size; // 4 bytes
	// TOTAL==36bytes

	public static final byte PAYLOAD_TYPE = (byte) 0x01;

	public Pong(Descriptor pongDesc, byte[] node_port, byte[] node_ip, byte[] file_count, byte[] total_size) {
		super();
		this.pongDesc = pongDesc;
		this.node_port = node_port;
		this.node_ip = node_ip;
		this.file_count = file_count;
		this.total_size = total_size;
	}

	public byte[] message() {
		byte[] msg = new byte[22 + 2 + 4 + 4 + 4];
		System.arraycopy(this.pongDesc.message(), 0, msg, 0, 22);
		System.arraycopy(this.node_port, 0, msg, 22, 2);
		System.arraycopy(this.node_ip, 0, msg, 24, 4);
		System.arraycopy(this.file_count, 0, msg, 28, 4);
		System.arraycopy(this.total_size, 0, msg, 32, 4);
		return msg;
	}

	public static Pong PongObjeFromMsg(byte[] msg) {
		Descriptor tmp = Descriptor.DescFromMsg(msg);
		byte[] node_p = new byte[2];
		byte[] node_ip = new byte[4];
		byte[] file = new byte[4];
		byte[] size = new byte[4];

		System.arraycopy(msg, 22, node_p, 0, 2);
		System.arraycopy(msg, 24, node_ip, 0, 4);
		System.arraycopy(msg, 28, file, 0, 4);
		System.arraycopy(msg, 32, size, 0, 4);

		return new Pong(tmp, node_p, node_ip, file, size);
	}

	public Descriptor getPongDesc() {
		return pongDesc;
	}

	public void setPongDesc(Descriptor pongDesc) {
		this.pongDesc = pongDesc;
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

	public byte[] getFile_count() {
		return file_count;
	}

	public void setFile_count(byte[] file_count) {
		this.file_count = file_count;
	}

	public byte[] getTotal_size() {
		return total_size;
	}

	public void setTotal_size(byte[] total_size) {
		this.total_size = total_size;
	}

}
