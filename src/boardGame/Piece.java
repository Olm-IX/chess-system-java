package boardGame;

public class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null; 
		// A POSI��O DE UMA PE�A RECEM CRIADA � NULA
		// MESMO SE N�O ATRIBU�SSE NULO, QUANDO CRIASSE O OBJETO
		// O ECLIPSE J� COLOCARIA NULO NO POSITION POR PADR�O
	}

	// PROTECTED, APENAS O AS CLASSES DO PACOTE OU SUBCLASSES
	// PODER�O ACESSAR O TABULEIRO DE UMA PE�A
	// O TABULEIRO N�O DEVE SER ACESSIVEL PELA CAMADA XADREZ
	protected Board getBoard() {
		return board;
	}
	
	
}
