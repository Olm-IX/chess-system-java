package application;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();

		while (!chessMatch.getCheckMate()) { // Repete enquanto não há cheque mate
			try {
				UI.clearScreen(); // Limpa a tela após cada jogada
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);

				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				// Imprime as possições possíveis a partir da peça escolhida:
				UI.printBoard(chessMatch.getPieces(), possibleMoves);

				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);

				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}

				if (chessMatch.getPromoted() != null) {
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String type = sc.nextLine();
					chessMatch.replacePromotedPiece(type);
				}

			} catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); // Aguardar a quebra de linha
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InvalidParameterException e) {
				String type = null;
				while (type == null) {
					System.out.println(e.getMessage());
					sc.nextLine();
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String string = sc.nextLine().toUpperCase();
					if (!(!string.equals("B") && !string.equals("N") && !string.equals("R") && !string.equals("Q"))) {
						type = string;
					}
				}
				chessMatch.replacePromotedPiece(type);
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}

}
