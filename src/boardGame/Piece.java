package boardGame;

public class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null; 
		// A POSIÇÃO DE UMA PEÇA RECEM CRIADA É NULA
		// MESMO SE NÃO ATRIBUÍSSE NULO, QUANDO CRIASSE O OBJETO
		// O ECLIPSE JÁ COLOCARIA NULO NO POSITION POR PADRÃO
	}

	// PROTECTED, APENAS O AS CLASSES DO PACOTE OU SUBCLASSES
	// PODERÃO ACESSAR O TABULEIRO DE UMA PEÇA
	// O TABULEIRO NÃO DEVE SER ACESSIVEL PELA CAMADA XADREZ
	protected Board getBoard() {
		return board;
	}
	
	
}
