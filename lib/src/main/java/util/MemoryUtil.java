package util;

public class MemoryUtil {

    public static void printMemoryUsage(){
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Get the total memory (in bytes) available to the JVM
        long totalMemory = runtime.totalMemory();
        // Get the free memory (in bytes) available to the JVM
        long freeMemory = runtime.freeMemory();
        // Calculate used memory
        long usedMemory = totalMemory - freeMemory;

        // Convert memory values to MB for easier readability
        long totalMemoryMB = totalMemory / (1024 * 1024);
        long freeMemoryMB = freeMemory / (1024 * 1024);
        long usedMemoryMB = usedMemory / (1024 * 1024);

        // Print memory information
        System.out.println("Total Memory (MB): " + totalMemoryMB);
        System.out.println("Free Memory (MB): " + freeMemoryMB);
        System.out.println("Used Memory (MB): " + usedMemoryMB);
    }
    
}
