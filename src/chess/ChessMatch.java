package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.security.InvalidParameterException;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
// ChessMatch conter� todas as regras do jogo de xadrez

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;

	// Construtor
	public ChessMatch() {
		board = new Board(8, 8); // Quem define a dimens�o de um tabuleiro de xadrez � a classe ChessMatch, e n�o o Board
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

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}

	// M�todos

	// Downcasting da matriz de Piece para uma matriz de ChessPiece
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j); // DOWNCASTING
			}
		}
		// A camada Chess n�o deve acessar a classe Piece, que pertence a outra camada.
		// E sim o ChessPiece, subclasse da camada Chess
		return mat;
	}

	// Cria uma matriz booleana com o valor "true" para os movimentos poss�veis da
	// pe�a que est� na posi��o dada
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	// Converte posi��o fornecida para posi��o de matriz, valida se posi��o e movimento s�o poss�veis, e executa o movimento
	// Retorna a pe�a que estava na posi��o destino (ou nulo)
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source); // Verifica se h� pe�a na posi��o inicial e se n�o est� "presa"
		validateTargetPosition(source, target); // Verifica se a pe�a escolhida pode ser movida para a posi��o destino
		Piece capturedPiece = makeMove(source, target);
		if (testCheck(currentPlayer)) { // Verifica se o jogador se autocolocou em cheque
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		ChessPiece movedPiece = (ChessPiece) board.piece(target);

		// Testa movimento especial Promo��o
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false; // Verifica se o jogador colocou o oponente em cheque
		if (testCheckMate(opponent(currentPlayer))) { // Verifica se o jogador colocou o oponente em cheque mate
			checkMate = true;
		} else {
			nextTurn(); // Troca o turno
		}
		// Movimento especial en passant
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}

		return (ChessPiece) capturedPiece;
	}
	
	// Troca o pe�o promovido para outra pe�a escolhida pelo usu�rio
	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
			throw new InvalidParameterException("Invalid type for promotion");
		}

		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);

		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	// Define qual foi a pe�a escolhida pelo usu�rio para promo��o
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}

	// Executa o movimento no tabuleiro
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		// Se houver pe�a na posi��o destino, a mesma � retirada da lista piecesOnTheBoard, e acrescentada na lista capturedPieces
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		// Caso haja roque pequeno, mover tamb�m a torre
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		// Caso haja roque grande, mover tamb�m a torre
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		// Verifica se houve movimento especial EnPassant
		if (p instanceof Pawn) {
			// Se o pe�o andou na diagonal e n�o capturou pe�a na posi��o destino, ent�o houve en passant
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					// Se o pe�o for branco, a pe�a a ser capturada est� abaixo dele
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}
				else {
					// Se o pe�o for preto, a pe�a a ser capturada est� acima dele
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return (ChessPiece) capturedPiece;
	}

	// Desfaz o movimento (retira a pe�a do destino e a coloca de novo na origem,
	// recoloco a pe�a capturada no destino, se houver)
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		// Desfazendo um roque pequeno
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// Desfazendo um roque grande
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// Desfazer jogada especial en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				}
				else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}

	// Verifica se � poss�vel mover a pe�a da posi��o inicial
	private void validateSourcePosition(Position position) {
		// Verifica se h� pe�a na posi��o
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		// Verifica se a pe�a na posi��o escolhida pertence ao jogador do turno
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		// Verifica se h� movimentos poss�veis (pe�a n�o est� "presa")
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}

	// Verifica se � poss�vel mover a pe�a escolhida para a posi��o final
	private void validateTargetPosition(Position source, Position target) {
		// Se para a pe�a de origem a posi��o de destino n�o � um movimento poss�vel
		// ent�o, lan�ar excess�o de movimento n�o poss�vel
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	// Pr�ximo turno da partida, muda a cor do jogador atual
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		// Express�o condicional tern�ria: ( condi��o ) ? valor_se_verdadeiro :
		// valor_se_falso
	}

	// Coloca nova pe�a no tabuleiro, usado no initial Setup
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		// Cada pe�a colocada � tamb�m inserida na lista de pe�as no tabuleiro,
		// piecesOnTheBoard
		piecesOnTheBoard.add(piece);
	}

	// Retorna a cor do oponente
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	// Localiza o rei de uma determinada cor
	private ChessPiece king(Color color) {
		// Filtra todas as pe�as da cor
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
		// Essa exce��o n�o ser� tratada porque nunca deve ocorrer, sempre deve haver o
		// rei na partida
		// Se ocorrer, � porque o programa precisa de ajustes
	}

	// O rei da cor est� em cheque? (varre todas as pe�as do tabuleiro e verifica se
	// h� movimento advers�rio capaz de capturar o rei)
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()] == true) {
				return true;
			}
		}
		return false;
	}

	// O rei da cor est� em cheque mate?
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) { // Testa se est� em cheque. Se n�o, n�o est� em cheque mate.
			return false;
		}
		// Filtra em lista todas as pe�as no tabuleiro da cor
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) { // Verifica todos os movimentos poss�veis das pe�as da lista, e se h� algum
								// capaz de tirar o rei do cheque mate
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) { // Se a posi��o da matriz � um movimento poss�vel da pe�a (true)
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target); // Faz o movimento
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece); // Desfaz o movimento
						if (!testCheck) {
							return false; // Movimento retirou o rei do cheque, ent�o n�o h� cheque mate
						}
					}
				}
			}
		}
		return true; // Nenhum movimento poss�vel de pe�as da cor retirou o rei do cheque
	}

	// Coloca as pe�as no tabuleiro
	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}
}
