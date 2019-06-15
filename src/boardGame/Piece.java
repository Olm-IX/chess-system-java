package boardGame;

public abstract class Piece {

	protected Position position;
	private Board board;

	// Construtor
	public Piece(Board board) {
		this.board = board;
		position = null; // A posi��o de uma pe�a rec�m criada � nula
		// Mesmo sem atribuir o nulo, o  eclipse j� colocaria por padr�o
	}

	// Encapsulamento
	protected Board getBoard() {
		// Protected: apenas as classes do pacote ou subclasses acessam
		// O tabuleiro n�o deve ser acess�vel por outras layers do UML
		return board;
	}
	
	// M�todos
	
	// M�todo abstrato
	// M�todos e classes abstratas possuem nome em it�lico no UML
	public abstract boolean[][] possibleMoves(); 
	
	
	// Hook method: m�todo concreto que chama uma implementa��o de um m�todo abstrato
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	// Verifica se a pe�a pode ser movida (true) ou est� presa (false)
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	
}
