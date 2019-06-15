package boardGame;
import boardGame.BoardException;

public class Board {
	
	private int rows;
	private int columns;
	private Piece [][] pieces;
	
	// Construtor
	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException ("Error creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece [rows][columns];
	}

	// Encapsulamento
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	//M�todos
	
	// Fornece a row e column e retorna a pe�a
	public Piece piece (int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[row][column];
	}
	
	// Sobrecarga do m�todo anterior, fornecendo a posi��o Position
	public Piece piece (Position position) {
		if (!positionExists(position.getRow(), position.getColumn())) {
			throw new BoardException("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	// Aloca a pe�a na posi��o
	public void placePiece (Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardException("There is already a piece on position" + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position; // Atributo position do objeto piece � definido como o argumento position
	}
	
	// Remove a pe�a da posi��o informada e retorna a pe�a removida
	public Piece removePiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException ("Position not on the board");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
	// Verifica se a row e column fornecidos existem dentro do tabuleiro, retorna true ou false
	public boolean positionExists (int row, int column) {
		return row >= 0 && row < rows && column >=0 && column < columns;
	}
	
	// Sobrecarga do m�todo anterior, fornecendo a posi��o Position
	public boolean positionExists (Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	// Verifica se a posi��o existe no tabuleiro. 
	// Se existir, verifica se h� pe�a na posi��o. Retorna true ou false
	public boolean thereIsAPiece (Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return piece(position) != null;
	}
}
