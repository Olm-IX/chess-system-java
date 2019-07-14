package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {
// ChessMatch conterá todas as regras do jogo de xadrez

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	// Construtor
	public ChessMatch() {
		board = new Board(8, 8); // Quem define a dimensão de um tabuleiro de xadrez é a classe ChessMatch, e não o Board
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	// Getters
	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}

	// Métodos

	// Downcasting da matriz de Piece para uma matriz de ChessPiece
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j); // DOWNCASTING
			}
		}
		// A camada Chess não deve acessar a classe Piece, que pertence a outra camada. E sim o ChessPiece, subclasse da camada Chess
		return mat;
	}

	// Cria uma matriz booleana com o valor "true" para os movimentos possíveis da peça que está na posição dada
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	// Converte posição fornecida para posição de matriz, valida se posição e movimento são possíveis, e executa o movimento
	// Retorna a peça que estava na posição destino (ou nulo)
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source); // Verifica se há peça na posição inicial e se não está "presa"
		validateTargetPosition(source, target); // Verifica se a peça escolhida pode ser movida para a posição destino
		Piece capturedPiece = makeMove(source, target);
		if (testCheck(currentPlayer)) { // Verifica se o jogador se autocolocou em cheque
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		check = (testCheck(opponent(currentPlayer))) ? true : false; // Verifica se o jogador colocou o oponente em cheque
		if (testCheckMate(opponent(currentPlayer))) { // Verifica se o jogador colocou o oponente em cheque mate
			checkMate = true;
		}
		else {
		nextTurn(); // Troca o turno
		}
		return (ChessPiece) capturedPiece;
	}

	// Executa o movimento no tabuleiro
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		// Se houver peça na posição destino, a mesma é retirada da lista piecesOnTheBoard, e acrescentada na lista capturedPieces
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return (ChessPiece) capturedPiece;
	}

	// Desfaz o movimento (retira a peça do destino e a coloca de novo na origem, recoloco a peça capturada no destino, se houver)
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}

	// Verifica se é possível mover a peça da posição inicial
	private void validateSourcePosition(Position position) {
		// Verifica se há peça na posição
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		// Verifica se a peça na posição escolhida pertence ao jogador do turno
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		// Verifica se há movimentos possíveis (peça não está "presa")
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}

	// Verifica se é possível mover a peça escolhida para a posição final
	private void validateTargetPosition(Position source, Position target) {
		// Se para a peça de origem a posição de destino não é um movimento possível
		// então, lançar excessão de movimento não possível
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	// Próximo turno da partida, muda a cor do jogador atual
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		// Expressão condicional ternária: ( condição ) ? valor_se_verdadeiro :
		// valor_se_falso
	}

	// Coloca nova peça no tabuleiro, usado no initial Setup
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		// Cada peça colocada é também inserida na lista de peças no tabuleiro,
		// piecesOnTheBoard
		piecesOnTheBoard.add(piece);
	}
	
	// Retorna a cor do oponente
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	// Localiza o rei de uma determinada cor
	private ChessPiece king(Color color) {
		// Filtra todas as peças da cor
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
		// Essa exceção não será tratada porque nunca deve ocorrer, sempre deve haver o rei na partida
		// Se ocorrer, é porque o programa precisa de ajustes
	}
	
	// O rei da cor está em cheque? (varre todas as peças do tabuleiro e verifica se há movimento adversário capaz de capturar o rei)
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()] == true) {
				return true;
			}
		}
		return false;
	}
	
	// O rei da cor está em cheque mate?
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) { // Testa se está em cheque. Se não, não está em cheque mate.
			return false;
		}
		// Filtra em lista todas as peças no tabuleiro da cor
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) { // Verifica todos os movimentos possíveis das peças da lista, e se há algum capaz de tirar o rei do cheque mate
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) { // Se a posição da matriz é um movimento possível da peça (true)
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target); // Faz o movimento
						boolean testCheck = testCheck(color); 
						undoMove(source, target, capturedPiece); // Desfaz o movimento
						if (!testCheck) {
							return false; // Movimento retirou o rei do cheque, então não há cheque mate
						}
					}
				}
			}
		}
		return true; // Nenhum movimento possível de peças da cor retirou o rei do cheque
	}	

	// Coloca as peças no tabuleiro
	private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));
        
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}
}
