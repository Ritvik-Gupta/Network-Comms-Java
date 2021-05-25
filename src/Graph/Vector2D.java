package src.Graph;

import java.awt.Dimension;
import java.awt.geom.Point2D;

public final class Vector2D {
	public final int x;
	public final int y;

	public Vector2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	@Override
	public String toString() {
		return "[ " + x + ", " + y + " ]";
	}

	public static boolean areEqual(Vector2D vecA, Vector2D vecB) {
		return vecA.x == vecB.x && vecA.y == vecB.y;
	}

	public static Vector2D splitSection(Vector2D vecA, Vector2D vecB, int m, int n) {
		return new Vector2D(
			Math.round((float)(m * vecB.x + n * vecA.x) / (float)(m + n)), 
			Math.round((float)(m * vecB.y + n * vecA.y) / (float)(m + n))
		);
	}

	public Vector2D add(Vector2D vec) {
		return new Vector2D(x + vec.x, y + vec.y);
	}

	public Vector2D add(int a, int b) {
		return new Vector2D(x + a, y + b);
	}

	public Vector2D multiply(int num) {
		return new Vector2D(this.x * num, this.y * num);
	}

	public boolean isContainedIn(Vector2D bottomVec, Vector2D topVec) {
		return x <= topVec.x && x >= bottomVec.x && y <= topVec.y && y >= bottomVec.y;
	}

	public Dimension asDimension() {
		return new Dimension(x, y);
	}

	public Point2D toPoint2D() {
		return new Point2D.Double(x, y);
	}
}
