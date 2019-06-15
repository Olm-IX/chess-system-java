package chess;

import boardGame.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	// Construtor
	public ChessPosition(char column, int row) {
		// Caracteres aceitam o comparativo de maior ou menor!!
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException ("Error instantiating ChessPosition. Valid values are from a1 to h8");
		}
		this.column = column;
		this.row = row;
	}

	// Encapsulamento
	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	// Transforma uma posição do tabuleiro de xadrez em uma posição da matriz
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
		// Irá subtrair o valor unicode do caracter pelo caracter 'a'. 'b-a' = 1, 'c-a' = 2, etc...
		// Assim encontramos que coluna da matriz corresponde o caracter do tabuleiro 
	}
	
	// Inverso do método anterior, transforma uma posição da matriz em uma posição do tabuleiro de xadrez
	protected static ChessPosition fromPosition (Position position) {
		return new ChessPosition ((char)('a' + position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}
	
}
