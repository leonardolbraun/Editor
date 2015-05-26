import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Editor extends JFrame implements ActionListener {

	private JPanel jPanelCampos;
	private JPanel jPanelBotoes2;
	private JPanel jpanelEstilos;

	private JTextPane textArea;

	private JButton btAbrir;
	private JButton bt2Gravar;
	private JButton bt2Sair;
	private JCheckBox negrito;
	private JCheckBox italico;
	private JCheckBox sublinhado;
	private JComboBox<String> fontes;
	private JComboBox<Integer> tamanho;
	private JScrollPane scroll;
	private JColorChooser escolherCor;
	private JButton botao;
	private Color cor;

	public Editor() {

		configuraJanela(); // configura o JFRAME
		initialize(); // Metodo que adiciona os panels ao JFrame
		scroll.add(textArea); // Adicionamos a Area de Texto ao Scroll
		scroll.setViewportView(textArea); //
		this.add(scroll); // Adiciona o Scroll ao JFrame, lembrando que aqui o
							// JScrollPane é um container para JTextPane
		setVisible(true); // Torna visivel a janela
	}

	private void configuraJanela() { // Método que configura nossa Janela
										// Principal (JFrame)
		setSize(840, 600); // Tamanho inicial da Janela
		this.setTitle("Editor GPL Free"); // Titulo que aparecerá no topo da
											// janela
		this.setLayout(new BorderLayout()); // Gerenciador de componentes da
											// Janela principal, nesse caso
											// estamos utilizando o BroderLayout
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Encerra a aplicação
														// assim que clicarmos
														// no botão fechar no
														// topo da janela
	}

	private void initialize() { // Método que adiciona nossos panel ao JFrame
		// this.add(getPanelBotoes2(), BorderLayout.CENTER);
		this.add(getPanelCampos2(), BorderLayout.SOUTH); // Adiciona o panel de
															// campos, nesse
															// caso está "this"
															// por se tratar da
															// própria classe
															// que extender
															// JFrame.
															// Adicionamos esse
															// panel na região
															// sul.
		this.add(getPanelEstilos(), BorderLayout.NORTH); // Adiciona o panel de
															// botões na região
															// norte
	}

	private void adicionaFontesNaCombo(JComboBox<String> combo) { // Método que adiciona
															// os botões no
															// combobox

		String confFontes = null;

		FileReader salvaConfiguracao = null;
		try {
			salvaConfiguracao = new FileReader(
					"C:/Users/Leonardo Braun/Editor/conf.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(salvaConfiguracao);
		String linha;
		StringBuffer sb = new StringBuffer();
		try {
			while ((linha = br.readLine()) != null) {
				sb.append(linha).append("\n");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		try {
			salvaConfiguracao.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		confFontes = sb.toString();

		if (confFontes.equals("")) {
			String[] fontes = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getAvailableFontFamilyNames();

			for (String fonte : fontes) {
				combo.addItem(fonte);
			}
		} else {

			combo.addItem(confFontes);

			String[] fontes = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getAvailableFontFamilyNames();

			for (String fonte : fontes) {
				combo.addItem(fonte);
				combo.removeItem(fonte.equals(confFontes));
			}
		}
	}

	private void adicionaTamanhoNaCombo(JComboBox<Integer> combo) { // Adiciona o tamanho
															// das fontes
		for (int i = 8; i <= 72; i += 2) { // Tamanho minimo de 8 e máximo de
											// 72. Cada passada pelo for é
											// acrescentado 2 ao numero
											// 8-10-12-14..32..48..72
			combo.addItem(i); // adiciona o tamanho atual ao combobox
		}
	}

	public void modificaEstilo() { // Modifica os estilos das fontes, tais como
									// negrito(bold), italico ou sublinhado..
		StyledDocument documento = (StyledDocument) textArea.getDocument();
		Style estilo = documento.getStyle(documento.addStyle("StyleAdd", null)
				.getName());

		// sublinhado, negrito, itálico
		StyleConstants.setBold(estilo, negrito.isSelected()); // Se negrito
																// estiver
																// marcado,
																// aplica o
																// StyleConstants.setBold
																// no texto
		StyleConstants.setItalic(estilo, italico.isSelected()); // Se o italico
																// estiver
																// marcado,
																// aplico o
																// StyleConstants.setItalic
																// no texto
		StyleConstants.setUnderline(estilo, sublinhado.isSelected()); // Se o
																		// sublinhado
																		// estiver
																		// marcado,
																		// aplica
																		// o
																		// StyleConstants.setUnderline
																		// no
																		// texto

		// cor
		if (cor != null) {// Se a cor for diferente de nula
			StyleConstants.setForeground(estilo, cor); // Chama o estilo e passa
														// a cor a ser aplicada
														// na fonte
		}

		// Salva na String fonte a fonte que está setada na ComboBox "fontes"
		String fonte = (String) fontes.getSelectedItem();

		// Essa parte salva a fonte escolhida em um arquivo localizado em um
		// arquivo de texto
		if (fonte != null) {

			StyleConstants.setFontFamily(estilo, fonte); // Chama o estilo e
															// passa qual fonte
															// deve ser aplicada
			FileOutputStream gravaConfiguracaoFonte = null;

			try {
				gravaConfiguracaoFonte = new FileOutputStream(
						"C:/Users/Leonardo Braun/Editor/conf.txt");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				gravaConfiguracaoFonte.write(fonte.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				gravaConfiguracaoFonte.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// tamanho da fonte
		int tamanhoFonte = 8;
		tamanhoFonte = (Integer) tamanho.getSelectedItem(); //Seleciona o tamanho que esta setado na combobox tamanho
		StyleConstants.setFontSize(estilo, tamanhoFonte); // Chama o estilo o passa o tamanho da fonte

		String textoFormatado = textArea.getText(); // A string textoFormatado recebe o texto contido em textArea
		textArea.setText(""); //Apaga o texto em textArea para não ficar repetido
		documento.addStyle("Style", estilo); // Adiciona o estilo ao documento com o nome de "Style"

		try {
			documento.insertString(documento.getLength(), textoFormatado,
				estilo);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Component getPanelEstilos() {
		if (jpanelEstilos == null) {
			jpanelEstilos = new JPanel();
			negrito = new JCheckBox("Negrito");
			italico = new JCheckBox("Itálico");
			sublinhado = new JCheckBox("Sublinhado");
			fontes = new JComboBox<String>();
			tamanho = new JComboBox<Integer>();
			scroll = new JScrollPane();
			escolherCor = new JColorChooser();
			cor = null;
			botao = new JButton("Escolher Cor"); // chama o actionPerformed lá
													// embaixo
			botao.addActionListener(this); // chama o actionPerformed lá embaixo
			negrito.addActionListener(this); // chama o actionPerformed lá
												// embaixo
			italico.addActionListener(this); // chama o actionPerformed lá
												// embaixo
			sublinhado.addActionListener(this); // chama o actionPerformed lá
												// embaixo
			fontes.addActionListener(this); // chama o actionPerformed lá
											// embaixo
			tamanho.addActionListener(this); // chama o actionPerformed lá
												// embaixo
			jpanelEstilos.add(negrito);
			jpanelEstilos.add(italico);
			jpanelEstilos.add(sublinhado);

			adicionaTamanhoNaCombo(tamanho);
			adicionaFontesNaCombo(fontes);
			tamanho.setSelectedIndex(3);
			fontes.setSelectedIndex(0);
			jpanelEstilos.add(fontes);
			jpanelEstilos.add(tamanho);
			jpanelEstilos.add(botao);

			jPanelBotoes2 = new JPanel();
			btAbrir = new JButton("Abrir");
			btAbrir.addActionListener(new ActionListener() { // Adiciona Evento
																// para o botão
																// abrir, ou
																// seja, quando
																// clicarmos em
																// Abrir

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JFileChooser chooserAbrirArquivo = new JFileChooser(); // Cria
																			// o
																			// componente
																			// para
																			// procurar
																			// um
																			// arquivo

					chooserAbrirArquivo.showOpenDialog(null); // Escolher
																// arquivo no PC

					FileReader fr = null;
					try {
						fr = new FileReader(chooserAbrirArquivo
								.getSelectedFile());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					BufferedReader br = new BufferedReader(fr);
					String linha;
					StringBuffer sb = new StringBuffer();
					try {
						while ((linha = br.readLine()) != null) {
							sb.append(linha).append("\n");
						}
					} catch (IOException e) {

						e.printStackTrace();
					}
					try {
						fr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					textArea.setText(sb.toString());
				}

			});

			bt2Gravar = new JButton("Gravar");
			bt2Gravar.addActionListener(new ActionListener() { // Adiciona
						// evento para o
						// botão gravar

						@Override
						public void actionPerformed(ActionEvent arg0) {

							File salvarArquivo = null;

							JFileChooser escolherCaminhoSalvar = new JFileChooser();
							escolherCaminhoSalvar
									.setFileSelectionMode(JFileChooser.FILES_ONLY);
							// file.set
							int i = escolherCaminhoSalvar.showSaveDialog(null);
							if (i == 1) {
								textArea.setText("");
							} else {
								salvarArquivo = escolherCaminhoSalvar
										.getSelectedFile();
								// textArea.setText(arquivo.getPath());
							}

							// String arquivoSalvar = textArea.getText();

							// arquivo = new File("C:/Teste/" + arquivoSalvar +
							// ".txt");
							FileOutputStream fos = null;
							try {
								fos = new FileOutputStream(salvarArquivo);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								fos.write(textArea.getText().getBytes());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								fos.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

			bt2Sair = new JButton("Sair");
			bt2Sair.addActionListener(new ActionListener() { // Adiciona evento
																// para o botão
																// sair

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0); // Quando clicar no botão sair essa linha
									// faz o programa ser terminado

				}
			});

			jpanelEstilos.add(btAbrir); // adiciona o botão ao panel de estilos
			jpanelEstilos.add(bt2Gravar); // adiciona o botão ao panel de
											// estilos
			jpanelEstilos.add(bt2Sair); // adiciona o botão ao panel de estilos

		}
		return jpanelEstilos;
	}

	private Component getPanelCampos2() {
		if (jPanelCampos == null) {
			jPanelCampos = new JPanel();
			textArea = new JTextPane(); // Instancia o textArea

			jPanelCampos.add(textArea); // Adiciona ao PanelCampos

		}
		return jPanelCampos;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "Escolher Cor") { // Se o campos estiver com o texto Escolher Cor
															// entra no if, senão passa direto para
															// modificaEstilo() ali embaixo;
			cor = escolherCor.showDialog(null, "Selecione: ", Color.RED); // Chama o JColorChooser passando
																			//o resultado escolhido para
																			// a variavel Color chamada cor
		}
		modificaEstilo(); 

	}

	// main para executar o aplicatico
	public static void main(String[] args) {
		new Editor();

	}

}