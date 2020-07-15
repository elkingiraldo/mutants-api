package co.com.elkin.apps.mutants.util;

public final class MatrixConverter {

	private final char[][] horizontal;
	private final char[][] vertical;
	private final int size;

	public MatrixConverter(final String[] matrix) {
		size = matrix.length;
		horizontal = toHorizontal(matrix);
		vertical = toVertical();
	}

	private char[][] toHorizontal(final String[] matrix) {
		final char[][] result = new char[size][size];

		for (char i = 0; i < size; i++) {
			result[i] = matrix[i].toCharArray();
		}

		return result;
	}

	private char[][] toVertical() {
		final char[][] result = new char[size][size];

		for (char i = 0; i < size; i++) {
			for (int j = 0; j < result.length; j++) {
				result[i][j] = horizontal[j][i];
			}
		}

		return result;
	}

	public char[][] getHorizontal() {
		return horizontal;
	}

	public char[][] getVertical() {
		return vertical;
	}

}
