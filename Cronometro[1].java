
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

// A classe Vector implementa um array redimensionável de objetos. Assim como um array, objetos da //classe Vector contém elementos que podem ser acessados via índices.

public class Cronometro extends WindowAdapter implements ActionListener{
        //Declaracao WindowAdapter-manipulador de eventos de janela, junto com ActionListener ()
        // windowOpened(),windowClosing(),windowClosed(), etc...
        private Frame janela;
        private Panel painelBotoes, painelEndereco;
        private Label lNomEQP1,lNomEQP2, lCronometro, lVoltasEQP1, lVoltasEQP2, lQEquipe;
        private Thread threadCronometro;
        private TextField tNomEQP1,tNomEQP2;
        private TextArea tVoltasEQP1, tVoltasEQP2;
        private Button bNovo, bSalvar,bExcluir,bProximo,bAnterior, bIniciar, bVolta, bParar;
        private CheckboxGroup cbgEQPV;
        private Checkbox Equipe1, Equipe2;
        private Vector vCronometro;
        private int posicao;
        private boolean rodando = false;

        
        Font fonteCronometro,fonteNome,fonteLabel;
         int minutos = 0;
         int segundos = 0;
         int milissegundos = 0;
        

        //Método Construtor Criacao de vetor, frame
        public Cronometro(){
                vCronometro=new Vector();
                janela = new Frame();
                janela.setTitle("Cronômetro");
                janela.setSize(700,615);
                janela.setBackground(new Color(40,40,40));
                janela.setLayout(null);
                janela.addWindowListener(this);

                //Método Construtor Criacao de Painel (backgroud principal)
                painelEndereco = new Panel();
                painelEndereco.setBackground(new Color(40,40,40));
                painelEndereco.setSize(700,500);
                painelEndereco.setLocation(8,28);
                painelEndereco.setLayout(null);

                //Método Construtor Criacao de Painel(backgroud dos botoes de salvar)
                painelBotoes = new Panel();
                painelBotoes.setBackground(new Color(217,79,79));
                painelBotoes.setSize(700,33);
                painelBotoes.setLocation(00,550);
                painelBotoes.setLayout(new FlowLayout());
                

                //Método Construtor Criacao de Labels
                //Nome Equipe 1
                lNomEQP1 = new Label("Nome da Equipe 1:");
                lNomEQP1.setForeground(new Color(255,255,255));
                fonteNome=new Font("Arial",Font.BOLD,20);
                lNomEQP1.setFont(fonteNome);
                //Nome Equipe 2
                lNomEQP2 = new Label("Nome da Equipe 2:");
                lNomEQP2.setForeground(new Color(255,255,255));
                fonteNome=new Font("Arial",Font.BOLD,20);
                lNomEQP2.setFont(fonteNome);
                //Campo voltas Equipe 1
                lVoltasEQP1 = new Label("Equipe 1:");
                lVoltasEQP1.setForeground(new Color(255,255,255));
                fonteNome=new Font("Arial",Font.BOLD,30);
                //Campo voltas Equipe 1
                lVoltasEQP2 = new Label("Equipe 2:");
                lVoltasEQP2.setForeground(new Color(255,255,255));
                fonteNome=new Font("Arial",Font.BOLD,30);
                //Contador cronometro
                lCronometro=new Label ("00:00:00");
                lCronometro.setBounds(200,180,260,60);
                lCronometro.setForeground(Color.white);
                fonteCronometro=new Font("Arial",Font.BOLD,64);
                lCronometro.setFont(fonteCronometro);
                //Qual Equipe ira correr
                lQEquipe = new Label("Qual equipe irá correr?");
                lQEquipe.setForeground(new Color(255,255,255));
                fonteNome=new Font("Arial",Font.BOLD,15);
                lQEquipe.setFont(fonteNome);


                //Método Construtor Criacao de TextFields
                tNomEQP1 = new TextField(20);
                fonteLabel=new Font ("Arial",Font.CENTER_BASELINE,17);
                tNomEQP1.setFont(fonteLabel);
                
                tNomEQP2 = new TextField(20);
                fonteLabel=new Font ("Arial",Font.CENTER_BASELINE,17);
                tNomEQP2.setFont(fonteLabel);



                //Substituicao do item pelo compomente especificado, na posicao indicada
                lNomEQP1.setBounds(10,25,180,20);
                tNomEQP1.setBounds(200,25,300,20);
                lNomEQP2.setBounds(10,55,180,20);
                tNomEQP2.setBounds(200,55,300,20);
                lQEquipe.setBounds(10,90,175,20);
                lVoltasEQP1.setBounds(10,170,250,300);
                lVoltasEQP2.setBounds(350,170,250,300);
                
                
                //Criacao de CheckboxGroup para definir a equipe a cronometrar a volta
        		cbgEQPV = new CheckboxGroup();
        		
        		//definindo texto da checkbox
        		Equipe1 = new Checkbox("Equipe 1",false,cbgEQPV);
        		Equipe2 = new Checkbox("Equipe 2",false,cbgEQPV);
        		
        		//definindo localizacao e tamanho
        		Equipe1.setBounds(200,91,90,19);
        		Equipe2.setBounds(300,91,90,19);
        		
        		//definindo fonte
        		Equipe1.setForeground(new Color(255,255,255));
                fonteNome=new Font("Arial",Font.BOLD,14);
                Equipe1.setFont(fonteNome);
                
                Equipe2.setForeground(new Color(255,255,255));
                fonteNome=new Font("Arial",Font.BOLD,14);
                Equipe2.setFont(fonteNome);



                //Método Construtor Criacao de de TextArea Equipe 1
                tVoltasEQP1 = new TextArea("",15,90,TextArea.SCROLLBARS_BOTH);
                tVoltasEQP1.setBounds(65,300,250,400);
                
                ////Método Construtor Criacao de de TextArea Equipe 2
                tVoltasEQP2 = new TextArea("",15,90,TextArea.SCROLLBARS_BOTH);
                tVoltasEQP2.setBounds(410,300,250,400);
                
                //Adiciona no frame painelEndereco os componentes criados
                painelEndereco.add(lNomEQP1);
                painelEndereco.add(tNomEQP1);
                painelEndereco.add(lNomEQP2);
                painelEndereco.add(tNomEQP2);
                painelEndereco.add(lQEquipe);
                painelEndereco.add(Equipe1);
                painelEndereco.add(Equipe2);
                painelEndereco.add(tVoltasEQP1);
                painelEndereco.add(tVoltasEQP2);
                painelEndereco.add(lVoltasEQP1);
                painelEndereco.add(lVoltasEQP2);



                //botoes
                bNovo = new Button("Novo");
                bNovo.addActionListener(this);
                bSalvar= new Button("Salvar");
                bSalvar.addActionListener(this);
                bExcluir = new Button("Exclui");
                bExcluir.addActionListener(this);
                bProximo=new Button("Próximo");
                bProximo.addActionListener(this);
                bAnterior=new Button("Anterior");
                bAnterior.addActionListener(this);
                
                //botao iniciar
                bIniciar = new Button("Iniciar");
                bIniciar.setBounds(570, 120, 100, 30); 
                bIniciar.addActionListener(this);
                janela.add(bIniciar); //adicionar diretamente à janela, não no painel
                janela.add(lCronometro);
                //Botao volta
                bVolta = new Button("Volta");
                bVolta.setBounds(570, 160, 100, 30); 
                bVolta.addActionListener(this);
                janela.add(bVolta);
                //Botao volta
                bParar = new Button("Parar");
                bParar.setBounds(570, 200, 100, 30); 
                bParar.addActionListener(this);
                janela.add(bParar);

                
                //Adicionando os botoes criados(Salvar)
                painelBotoes.add(bNovo);
                painelBotoes.add(bSalvar);
                painelBotoes.add(bExcluir);
                painelBotoes.add(bProximo);
                painelBotoes.add(bAnterior);
                

                //adiciona na Janela os frames
                janela.add(painelEndereco);
                janela.add(painelBotoes);
            

                //Desabilita os botoes quando inicia o programa
                bExcluir.setEnabled(false);
                bAnterior.setEnabled(false);
                bProximo.setEnabled(false);
                
                //Procurar uma forma de desabilitar ele apos o primeiro clique
                /*bIniciar.setEnabled(true);*/
        }

        //Adiciona o conteudo no vetor(setText)
        public void setNomEQP1(String NomEQP1){
                tNomEQP1.setText(NomEQP1);
        }
        
        public void setNomEQP2(String NomEQP2){
            tNomEQP2.setText(NomEQP2);
    }
        public void setVoltasEQP1(String VoltasEQP1){ tVoltasEQP1.setText(VoltasEQP1);
        }
        public void setVoltasEQP2(String VoltasEQP2){
                tVoltasEQP2.setText(VoltasEQP2);
        }
        //
        public void setEQPV(String EQPV) {
    		if (EQPV != null)	{
    			if (EQPV.equals("1")) Equipe1.setState(true);
    			else if (EQPV.equals("2")) Equipe2.setState(true);
    		}
    	}




        //Retorna o conteudo(=objeto) que esta no vetor
        public String getNomEQP1(){
                return tNomEQP1.getText();
        }
        public String getNomEQP2(){
            return tNomEQP2.getText();
        }
        public String getVoltasEQP1(){
                return tVoltasEQP1.getText();
        }
        public String getVoltasEQP2(){
                return tVoltasEQP2.getText();
        }


        //Verificacao de qual dos botoes foi acionado para chamar os procedimentos devidos
        public void actionPerformed(ActionEvent e)	{
                Button b=(Button)e.getSource();
                if (b==bNovo)           this.botaoNovo();
                else if (b==bIniciar)   this.botaoIniciar();
                else if (b==bSalvar)	   this.botaoSalvar();
                else if (b==bExcluir)   this.botaoExcluir();
                else if (b==bAnterior) this.botaoAnterior();
                else if (b==bProximo)  this.botaoProximo();
                else if (b == bIniciar) this.botaoIniciar();
                else if (b == bParar) 	  	this.botaoParar();
                else if (b == bVolta)   	this.botaoVolta();
        }

        //Acao do botao Novo
        void botaoNovo() {
                    this.limpaDados();
                    bExcluir.setEnabled(false);
                    bAnterior.setEnabled(false);

                    bProximo.setEnabled(false);
                    tNomEQP1.requestFocus();
            }

        //Acao do botao salva (estava com problema)
        public void botaoSalvar() {
            //Pega os valores dos campos de texto
            vCronometro.addElement(new InfCronometro(getNomEQP1(),getNomEQP2(), getVoltasEQP1(), getVoltasEQP2()));
		bExcluir.setEnabled(true);
		bAnterior.setEnabled(true);
		bProximo.setEnabled(true);
	}

            // Cria o objeto InfCronometro com os dados coletados
           /* InfCronometro Cronometro = new InfCronometro(Nome, voltas);

            // Adiciona o objeto ao vetor
            vCronometro.addElement(Cronometro);

            // Habilita os botões de navegação
            bExcluir.setEnabled(true);
            bAnterior.setEnabled(true);
            bProximo.setEnabled(true);
        }*/
        
        private String formattempo(int totsegundos) {
        	return String.format("%02d:%02d:%02d", minutos, segundos, milissegundos);
        }
        



        //Acao do botao Exclui
        void botaoExcluir() {
                if(!vCronometro.isEmpty())
                        vCronometro.removeElementAt(posicao);
                tNomEQP1.requestFocus();
                }

        //Acao do botao Anterior
        void botaoAnterior() {
                if(!vCronometro.isEmpty()){
                        bProximo.setEnabled(true);
                        --posicao;
                        if(posicao<=0) {
                                posicao=0;
                                bAnterior.setEnabled(false);
                        }
                        this.obterDadosVeiculo(posicao);
                }
        }

        //Acao do botao Proximo
        void botaoProximo() {
                if(!vCronometro.isEmpty()){
                        bAnterior.setEnabled(true);
                        ++posicao;
                        if(posicao>=vCronometro.size()-1) {
                                posicao=vCronometro.size()-1;
                                bProximo.setEnabled(false);
                        }
                        this.obterDadosVeiculo(posicao);
                }
        }

        //Limpa Campos
        public void limpaDados()	{
                this.setNomEQP1("");
                this.setNomEQP2("");
                this.setVoltasEQP1("");
                this.setVoltasEQP2("");

        }

        //Obtem dados
        public void obterDadosVeiculo(int pos) {
                InfCronometro veiculoAtual=(InfCronometro)vCronometro.elementAt(pos);
                this.setNomEQP1(veiculoAtual.getNomEQP1());
                this.setNomEQP2(veiculoAtual.getNomEQP2());
                this.setVoltasEQP1(veiculoAtual.getVoltasEQP1());
                this.setVoltasEQP2(veiculoAtual.getVoltasEQP2());


        }


    //fecha janela
        public void windowClosing(WindowEvent e) {
                System.exit(0);
        }

        public void mostraCronometro(){
                janela.setVisible(true);
        }
        
        void botaoIniciar() {
            if (rodando) return;
            bIniciar.setEnabled(false);

            rodando = true;
            threadCronometro = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (rodando) {
                            Thread.sleep(10);  // A contagem vai de 10 em 10 milissegundos

                            milissegundos++;  // Aumenta milissegundos

                            if (milissegundos >= 100) {
                                milissegundos = 0;
                                segundos++;  // Aumenta segundos a cada 100 milissegundos
                            }

                            if (segundos >= 60) {
                                segundos = 0;
                                minutos++;  // Aumenta minutos a cada 60 segundos
                            }

                            lCronometro.setText(formattempo(milissegundos));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadCronometro.start();
        }


        
        void botaoParar() {
            bIniciar.setEnabled(true);
        	if (rodando) {
        		rodando = false;
        	}
        	else {
        		minutos=0;
                segundos=0;
                milissegundos=0;
                lCronometro.setText(formattempo(milissegundos));
              
        	}
			
            
            
            
        }
     /*
        void botaoVolta() {
            String tempo = formattempo(milissegundos);  // Usa o novo formato

            if (Equipe1.getState(true)) {

            }

            if (Equipe2.getState(true)){

            }
            tVoltas.append("Volta: " + tempo + "\n");
        }*/


    DecimalFormat casas = new DecimalFormat("00");
    Vector<String> volta = new Vector<>();
    public static String tempoVolta;
    public static String numVolta;

    public void botaoVolta() {

        tempoVolta = casas.format(minutos) +":"+ casas.format(segundos) +":"+ casas.format(milissegundos);
        volta.add(tempoVolta);


        //Cria as voltas para adc no BD
        for (int a=0 ; a < volta.size(); a++){
            numVolta = "Volta " + a;
        }


        //Mostra na caixa do cronometro
        Vector todasVoltas = new Vector<>();
         for (int i = 0; i < volta.size(); i++){
            todasVoltas.add("Volta " + i + " --" + volta.get(i)+ "\n" );
        }
        tVoltasEQP1.setText(todasVoltas.toString());


         //Chama a ação do BD para salvar o tempo - tempoVolta <-
        try {
            bancodedados();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }




    public static void bancodedados() throws SQLException {
        Connection conexao = null;
        String url = "jdbc:mysql://localhost:3306/corrida";
        String user = "root";
        String password = "";




        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = DriverManager.getConnection(url,user,password);
            System.out.println("Conectado com sucesso.");

            //Adiciona as voltas no BD
            String sql = "insert into voltas (numero_volta,tempo_volta) values (?, ?) ";
            PreparedStatement insercao = conexao.prepareStatement(sql);
            insercao.setString(1, numVolta);
            insercao.setString(2, tempoVolta);

            int linhasafetadas = insercao.executeUpdate();
            System.out.println("Row Affected "+ linhasafetadas);

        } catch (ClassNotFoundException e) {
            System.out.println("Driver do BD não localizado.");
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao acessar o BD: "+ e.getMessage());
        } finally {
            if (conexao != null){
                conexao.close();
            }

        }



    }








        
    public static void main(String[] args) throws SQLException {

        Cronometro Cronometro = new Cronometro();
        Cronometro.mostraCronometro();
        bancodedados();

    }
}
