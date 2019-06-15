package boardGame;

public abstract class Piece {

	protected Position position;
	private Board board;

	// Construtor
	public Piece(Board board) {
		this.board = board;
		position = null; // A posição de uma peça recém criada é nula
		// Mesmo sem atribuir o nulo, o  eclipse já colocaria por padrão
	}

	// Encapsulamento
	protected Board getBoard() {
		// Protected: apenas as classes do pacote ou subclasses acessam
		// O tabuleiro não deve ser acessível por outras layers do UML
		return board;
	}
	
	// Métodos
	
	// Método abstrato
	// Métodos e classes abstratas possuem nome em itálico no UML
	public abstract boolean[][] possibleMoves(); 
	
	
	// Hook method: método concreto que chama uma implementação de um método abstrato
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	// Verifica se a peça pode ser movida (true) ou está presa (false)
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
