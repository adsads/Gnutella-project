package node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import messages.Descriptor;
import messages.Ping;
import messages.Pong;
import messages.Query;
import utils.Constants;
import utils.Conversion;
import utils.Neighbor;

public class Node extends Thread {

	public byte[] ip; // 4
	public byte[] port; // 2
	public byte[] speed; // 2
	public byte[] file_count; // 4
	public byte[] total_size; // 4

	public String shareFolder;
	private File shareFolderFile;

	private Descriptor desc;
	public ArrayList<Neighbor> neighbors = new ArrayList<>();
	public ArrayList<File> shareList = new ArrayList<>();

	public Node(String dir, byte[] ip, byte[] port, byte[] speed) {
		super();
		this.ip = ip;
		this.port = port;
		this.speed = speed;
		this.shareFolder = Constants.BASE_DIR + dir + "/";

		shareFolderFile = new File(this.shareFolder);
		if (!shareFolderFile.exists()) // make it
		{
			try {
				shareFolderFile.mkdir();
				shareFolderFile.setExecutable(true, false);
				shareFolderFile.setReadable(true, false);
				shareFolderFile.setWritable(true, false);
			} catch (SecurityException e) {
				System.err.println("You have insufficient permissions to do this.");
			}
		}

		try {
			this.neighbors = LoadNeighbors();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't load neighbor list");
		}

		this.shareList = LoadShare();
		this.file_count = Conversion.intToBytes(shareList.size());
		int sum = 0;
		for (File f : this.shareList) {
			sum += f.length();
		}
		this.total_size = Conversion.intToBytes(sum);
		this.desc = new Descriptor(null, this.ip, this.port);

	}

	public byte[] getNodeId() {
		return this.desc.getID();
	}

	public void setNodeId(byte[] nodeId) {
		this.desc.setID(nodeId);
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

	public String getShareFolder() {
		return shareFolder;
	}

	public void setShareFolder(String shareFolder) {
		this.shareFolder = shareFolder;
	}

	public File getShareFolderFile() {
		return shareFolderFile;
	}

	public void setShareFolderFile(File shareFolderFile) {
		this.shareFolderFile = shareFolderFile;
	}

	public ArrayList<Neighbor> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Neighbor> neighbors) {
		this.neighbors = neighbors;
	}

	public ArrayList<File> getShareList() {
		return shareList;
	}

	public void setShareList(ArrayList<File> shareList) {
		this.shareList = shareList;
	}

	@SuppressWarnings({ "unchecked" })
	private ArrayList<Neighbor> LoadNeighbors() throws FileNotFoundException, IOException, ClassNotFoundException {
		ArrayList<Neighbor> tmp = new ArrayList<Neighbor>();
		@SuppressWarnings("resource")
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(this.shareFolder + ".nlist"));
		tmp = (ArrayList<Neighbor>) inputStream.readObject();
		return tmp;
	}

	private void SaveNeighbors() throws IOException {
		@SuppressWarnings("resource")
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(this.shareFolder + ".nlist"));
		outputStream.writeObject(this.neighbors);
	}

	private ArrayList<File> LoadShare() {
		if (!this.shareFolderFile.isDirectory()) {
			System.err.println("Not a directory!");
			System.exit(-1);
		}
		ArrayList<File> ret = new ArrayList<File>();
		File[] filelist = this.shareFolderFile.listFiles();
		for (File f : filelist)
			if (f.isFile())
				ret.add(f);
		return ret;
	}

	public static void main(String args[]) throws IOException {
		if (args.length != 3) {
			System.err.println("Proper usage includes two args. port and speed.");
			System.exit(-1);
		}
		short port = Short.parseShort(args[0]);
		short speed = Short.parseShort(args[1]);
		String dir = args[2];

		Node mine = new Node(dir, Constants.LOCAL_HOST, Conversion.shortToBytes(port), Conversion.shortToBytes(speed));
		DatagramSocket mineSocket = new DatagramSocket(port);

		// ensure neighbors get saved
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					mine.SaveNeighbors();
					mineSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mine.start(); // ping previous neighbors, if any

		if (null == mine.neighbors || mine.neighbors.isEmpty()) {
			mine.pingKnownHosts();
		} // if no existing nodes, ping super-nodes

		while (true) {
			int buffSize = mineSocket.getReceiveBufferSize();

			byte[] recvbuff = new byte[buffSize];

			DatagramPacket receivePacket = new DatagramPacket(recvbuff, recvbuff.length);
			mineSocket.receive(receivePacket);

			byte[] recvData = new byte[receivePacket.getLength()];
			System.arraycopy(receivePacket.getData(), 0, recvData, 0, receivePacket.getLength());

			byte[] payload_type = new byte[2];
			System.arraycopy(receivePacket.getData(), 16, payload_type, 0, 2);
			switch (Conversion.bytesToShort(payload_type)) {
			case 0x00: { // ping
				Ping recvPing = Ping.PingFromMsg(recvData);
				Neighbor newNeigh = new Neighbor(recvPing.getSender_node_ip(), recvPing.getSender_node_port());
				mine.desc.setPayloadType(Pong.PAYLOAD_TYPE);
				Pong sendPong = new Pong(mine.desc, mine.getPort(), mine.getIp(), mine.getFile_count(),
						mine.getTotal_size());

				// reply with pong then forward ping if TIL valid
				DatagramPacket pongdp = new DatagramPacket(sendPong.message(), sendPong.message().length,
						InetAddress.getByAddress(newNeigh.getIp()), Conversion.bytesToShort(newNeigh.getPort()));
				mineSocket.send(pongdp);

				if (recvPing.pingDesc.getTTL() > recvPing.pingDesc.hops) {
					// VALID keep sending
					for (Neighbor n : mine.neighbors) {
						DatagramPacket dp = new DatagramPacket(recvPing.message(), recvPing.message().length,
								InetAddress.getByAddress(n.getIp()), Conversion.bytesToShort(n.getPort()));
						mineSocket.send(dp);
					}
				}
				mine.neighbors.add(newNeigh);
				break;
			}
			case 0x01: { // pong
				Pong recvPong = Pong.PongObjeFromMsg(recvData);
				Neighbor newNeigh = new Neighbor(recvPong.getNode_ip(), recvPong.getNode_port());
				mine.neighbors.add(newNeigh);
				break;
			}
			case 0x80: // query
			{
				Query recvQuery = Query.QueryFromMsg(recvData);
				if (Conversion.bytesToShort(recvQuery.getSpeed()) > Conversion.bytesToShort(mine.getSpeed()))
					break;

				for (File f : mine.shareList) {
					if (f.getAbsolutePath().contains(Conversion.bytesToString(recvQuery.getSearchStr())))
						System.out.println("Node:" + mine.getNodeId() + " has a match.");
				}
				// forward query if TIL valid

				if (recvQuery.getQueryDesc().getTTL() > recvQuery.getQueryDesc().hops) {
					// VALID keep sending
					for (Neighbor n : mine.neighbors) {
						DatagramPacket dp = new DatagramPacket(recvQuery.message(), recvQuery.message().length,
								InetAddress.getByAddress(n.getIp()), Conversion.bytesToShort(n.getPort()));
						mineSocket.send(dp);
					}
				}
			}
			}
		}

	}

	private void pingKnownHosts() {
		try {
			DatagramSocket ds = new DatagramSocket();
			this.desc.setHops((byte) 0);
			this.desc.setTTL((byte) 5);
			this.desc.setPayloadLength(Conversion.intToBytes(0));
			Ping pingNeigh = new Ping(this.desc, this.getIp(), this.getPort());

			for (Neighbor n : Constants.KNOWN_NODES) {
				DatagramPacket dp = new DatagramPacket(pingNeigh.message(), pingNeigh.message().length,
						InetAddress.getByAddress(n.getIp()), Conversion.bytesToShort(n.getPort()));
				ds.send(dp);
			}
			ds.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() { // handle pining neighbors every 2 minutes
		while (true) {
			try {
				Thread.sleep(120000);
				System.out.println("Node: " + this.getNodeId() + " Sending pings to neighbors");

				DatagramSocket ds = new DatagramSocket();
				this.desc.setHops((byte) 0);
				this.desc.setTTL((byte) 5);
				this.desc.setPayloadLength(Conversion.intToBytes(0));
				Ping pingNeigh = new Ping(this.desc, this.getIp(), this.getPort());

				for (Neighbor n : this.neighbors) {
					DatagramPacket dp = new DatagramPacket(pingNeigh.message(), pingNeigh.message().length,
							InetAddress.getByAddress(n.getIp()), Conversion.bytesToShort(n.getPort()));
					ds.send(dp);
				}
				ds.close();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
