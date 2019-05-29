package chess;

import boardGame.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		//CLASSE PARA CONVERTER A POSI플O DA MATRIZ EM UMA POSI플O DO TABULEIRO DE XADREZ
		//CARACTERES ACEITAM OS COMPARATIVOS DE MAIOR OU MENOR!
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException ("Error instantiating ChessPosition. Valid values are from a1 to h8");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	//TRANSFORMAR UMA POSI플O DO TABULEIRO DE XADREZ EM UMA POSI플O DA MATRIZ
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
		//IR� SUBTRAIR O VALOR UNICODE DO CARACTER PELO CARACTER 'a'. 'b-a' = 1, 'c-a' = 2, etc...
		//ASSIM ENCONTRAMOS QUE COLUNA DA MATRIZ CORRESPONDE O CARACTER DO TABULEIRO 
	}
	
	//TRANSFORMAR UMA POSI플O DA MATRIZ EM UMA POSI플O DO TABULEIRO DE XADREZ
	protected static ChessPosition fromPosition (Position position) {
		return new ChessPosition ((char)('a' + position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}
	
}
