public class PacketSymmetryAuditor {
    public static void main(String[] args) {
        String payload = "RADAR";

        int length = payload.length();
        TransactionStack stack = new TransactionStack(length);
        RequestQueue queue = new RequestQueue(length);

        for (int i = 0; i < length; i++) {
            char c = payload.charAt(i);
            stack.push(c);
            queue.enqueue(c);
        }

        boolean isSymmetric = true;

        while (!stack.isEmpty() && !queue.isEmpty()) {
            if (stack.pop() != queue.dequeue()) {
                isSymmetric = false;
                break;
            }
        }

        System.out.println("--- Edge Security Gateway Audit ---");
        System.out.println("Payload: " + payload);
        if (isSymmetric) {
            System.out.println("Result: VERIFIED (Symmetric Palindrome Signature Detected)");
        } else {
            System.out.println("Result: REJECTED (Asymmetric Payload)");
        }
    }
}