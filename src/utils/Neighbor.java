package utils;

import java.io.Serializable;

public class Neighbor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3207340473301529009L;

	public byte[] ip; // 4
	public byte[] port; // 2
	public byte[] speed; // 2
	public byte[] file_count; // 4
	public byte[] total_size; // 4

	public Neighbor(byte[] ip, byte[] port) {
		this.ip = ip;
		this.port = port;
		this.setFile_count(Conversion.intToBytes(0));
		this.setTotal_size(Conversion.intToBytes(0));
	}

	public byte[] getIp() {
		return ip;
	}

	public void setIp(byte[] ip) {
		this.ip = ip;
	}

	public byte[] getPort() {
		return port;
	}

	public void setPort(byte[] port) {
		this.port = port;
	}

	public byte[] getSpeed() {
		return speed;
	}

	public void setSpeed(byte[] speed) {
		this.speed = speed;
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
