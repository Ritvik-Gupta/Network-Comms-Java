package src.Hamming;

import java.util.ArrayList;
import java.util.List;

public final class HammingCode {
	private static boolean hasEvenOnes(ArrayList<Boolean> bits) {
		return bits.stream().reduce(0, (total, isOne) -> total + (isOne ? 1 : 0), Integer::sum) % 2 == 0;
	}

	private static boolean hasParityBit(int num, int parityBit) {
		return (num & parityBit) == parityBit;
	}

	public static String padToHammingSize(String bits) {
		StringBuilder str = new StringBuilder();
		str.append("000");
		str.append(bits.charAt(0));
		str.append("0");
		str.append(bits.substring(1, 4));
		str.append("0");
		str.append(bits.substring(4, 7));
		return str.toString();
	}

	private static ArrayList<Boolean> parseBits(String bits) throws Exception {
		ArrayList<Boolean> parsedBits = new ArrayList<>();
		for (int pos = 0; pos < bits.length(); ++pos) {
			if (bits.charAt(pos) == '1')
				parsedBits.add(true);
			else if (bits.charAt(pos) == '0')
				parsedBits.add(false);
			else
				throw new Exception("A Bit Representaion can only have 1 and 0 is the Unparsed Bit String");
		}
		return parsedBits;
	}

	private final ArrayList<Boolean> transmission;

	public HammingCode(String transmission) throws Exception {
		if (transmission.length() == 7)
			transmission = padToHammingSize(transmission);
		this.transmission = parseBits(transmission);
		this.setHammingBits();
	}

	public HammingCode(ArrayList<Boolean> transmission) {
		this.transmission = transmission;
		this.setHammingBits();
	}

	public void print() {
		for (int pos = 0; pos < transmission.size(); ++pos)
			System.out.print(((pos & (pos - 1)) == 0 ? "P" : " ") + "\t");
		System.out.println();
		transmission.forEach(isOne -> System.out.print(isOne + "\t"));
		System.out.println();
	}

	private void flipBit(int pos) {
		transmission.set(pos, !transmission.get(pos));
	}

	public Integer errorCorrection() {
		if (hasEvenOnes(transmission))
			return null;

		int errorPos = 0, parityBitPos = 1;
		while (parityBitPos < transmission.size()) {
			ArrayList<Boolean> parityBitCover = new ArrayList<>();
			for (int pos = 0; pos < transmission.size(); ++pos)
				if (hasParityBit(pos, parityBitPos))
					parityBitCover.add(transmission.get(pos));

			if (!hasEvenOnes(parityBitCover))
				errorPos += parityBitPos;
			parityBitPos *= 2;
		}
		flipBit(errorPos);
		return errorPos;
	}

	private void setHammingBits() {
		int parityBitPos = 1;
		while (parityBitPos < transmission.size()) {
			ArrayList<Boolean> parityBitCover = new ArrayList<>();
			for (int pos = 0; pos < transmission.size(); ++pos)
				if (hasParityBit(pos, parityBitPos))
					parityBitCover.add(transmission.get(pos));

			if (!hasEvenOnes(parityBitCover))
				flipBit(parityBitPos);
			parityBitPos *= 2;
		}
		if (!hasEvenOnes(transmission))
			flipBit(0);
	}

	public HammingCode manualNoise(List<Integer> noisePositions) {
		HammingCode receiverNode = new HammingCode(new ArrayList<>(transmission));
		noisePositions.forEach(pos -> receiverNode.flipBit(pos));
		return receiverNode;
	}
}
