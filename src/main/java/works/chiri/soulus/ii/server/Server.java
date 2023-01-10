package works.chiri.soulus.ii.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.jline.utils.InputStreamReader;

import works.chiri.soulus.ii.SoulusII;


public class Server extends Thread {

	private ServerSocket serverSocket;
	private final int port;

	public Server (int port) {
		this.port = port;
	}

	@Override
	public void run () {
		final String prefix = "Socket server " + port + ": ";
		try {
			serverSocket = new ServerSocket(port);
			SoulusII.LOGGER.info(prefix + "Opened server");

		}
		catch (IOException e) {
			SoulusII.LOGGER.error(prefix + "Error opening server", e);
		}

		Set<Client> clients = new HashSet<>();

		try {
			while (true) {
				if (isInterrupted())
					break;

				Socket socket = null;
				try {
					socket = serverSocket.accept();
					SoulusII.LOGGER.info(prefix + "Accepted client");

					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();

					Client client = new Client(socket, in, out);
					client.start();
					clients.add(client);
				}
				catch (IOException e) {
					SoulusII.LOGGER.error(prefix + "Error accepting client", e);
				}

			}

		}
		finally {
			try {
				serverSocket.close();
				SoulusII.LOGGER.info(prefix + "Closed server");
				for (Client client : clients)
					client.interrupt();
			}
			catch (IOException e) {
				SoulusII.LOGGER.error(prefix + "Error closing server", e);
			}

		}

	}

	public static class Client extends Thread {

		public static int id = 0;

		// private final Socket socket;
		private final InputStream in;
		private final OutputStream out;

		public Client (Socket socket, InputStream in, OutputStream out) {
			// this.socket = socket;
			this.in = in;
			this.out = out;
		}

		@Override
		public void run () {
			final int id = Client.id++;
			final String prefix = "Socket client " + id + ": ";

			try {

				try {
					Writer writer = new BufferedWriter(new OutputStreamWriter(out, "utf8"));
					writer.append(Serialiser.getTypeScriptDeclarations())
						.append("\n")
						.append(ActionManager.getTypeScriptDeclarations())
						.flush();
				}
				catch (final IOException e) {

				}

				while (true) {
					if (isInterrupted())
						break;

					try {
						try {
							in.available();
						}
						catch (IOException e) {
							break;
						}

						SoulusII.LOGGER.info(prefix + "Input:" + inputStreamAsString(in));
					}
					catch (IOException e) {
						if (!e.getMessage().equals("Stream closed."))
							SoulusII.LOGGER.error(prefix + "Error processing input ", e);
						break;
					}

				}

			}
			finally {
				try {
					in.close();
					out.close();
					SoulusII.LOGGER.info(prefix + "Closed client");
				}
				catch (IOException e) {
					SoulusII.LOGGER.error(prefix + "Error closing client", e);
				}

			}

		}

	}

	public static String inputStreamAsString (InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}

		br.close();
		return sb.toString();
	}

}
