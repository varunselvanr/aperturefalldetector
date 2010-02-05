package com.android.falldetector;
import android.hardware.Sensor;
import android.util.Log;

/* Our prototype's algorithm uses an Android-enabled phone's Accelerometer and
 * orientation sensors to detect if a fall has occurred with the phone attached
 * to the user. (or in pocket, hopefully!) It looks for a particular pattern among
 * 20 data points (for now). If it sees an unexpected change in variance among one
 * of the axes of those data points it looks for 
 */

/* Not finished: 12/5/2009 zschallz */
public class fallData {

	/* Constants */
	public static final int 	numDataPoints 	= 20;
	public static final int		oriThreshold 	= 60;
	public static final float 	accThreshold 	= (float) .50;
	
	/* Indices */
	private static int 			curAPRDataPoint = 0;
	private static int 			curXYZDataPoint	= 0;
	
	/* Phone's recent orientation data
	 * These are multidimensional arrays that share an index.
	 * As per advice from http://developer.android.com/guide/practices/design/performance.html#object_creation
	 */
	private static int[] 	azimuth 	= new int[numDataPoints]; // http://developer.android.com/guide/practices/design/performance.html#avoidfloat
	private static int[] 	pitch 		= new int[numDataPoints];
	private static int[] 	roll 		= new int[numDataPoints];
	private static long[]	timestamps 	= new long[numDataPoints];
		
	private static int 		avgAzimuth 	= 0;
	private static int 		avgPitch 	= 0;
	private static int 		avgRoll 	= 0;
	
	/* Phone's recent acceleration data */
/*	private static float[] 	x 			= new float[numDataPoints];
	private static float[] 	y 			= new float[numDataPoints];
	private static float[] 	z 			= new float[numDataPoints];
	
	private static float 	avgX 		= 0;
	private static float 	avgY 		= 0;
	private static float 	avgZ 		= 0;*/
	
	/* Other local variables */
	private static boolean	initialOriFallDetected = false;
	private static boolean 	initialAccFallDetected = false;
	private static int 		sinceInitialDetect;
	
	/* Adds data point and calls fall check */
/*	public static void addData(float theX, float theY, float theZ) //for accelerometer data
	{
		x[curXYZDataPoint]			= theX;
		y[curXYZDataPoint]			= theY;
		z[curXYZDataPoint]			= theZ;
		
		checkDataVariance( x, y, z );
	}
*/
	public static void addData(int theX, int theY, int theZ, long timestamp) //for orientation data
	{
		azimuth[curAPRDataPoint] 	= theX;
		pitch[curAPRDataPoint] 		= theY;
		roll[curAPRDataPoint] 		= theZ;
		timestamps[curAPRDataPoint] = timestamp;
		
		checkDataVariance( azimuth, pitch, roll );
	}
	
	/* Calculates variance among 20 recent data points collected from each axis.
	 * If we detect a sudden variance between data points we look further into it 
	 * a few seconds later to see if the variance stopped.
	 * (I.E. Sudden downward acceleration value returned from accelerometer and
	 * sudden orientation change, then *thud*... normalcy)
	 */
//	public static void checkDataVariance(float[] theX, float[] theY, float[] theZ) //for accelerometer data
//	{
			/* Data Analysis */
						
			/* Data Recording */
//			avgX 	= calcAverage( theX );
//			avgY 	= calcAverage( theY );
//			avgZ 	= calcAverage( theZ );
//			
//			if( curXYZDataPoint < numDataPoints-1 )
//				curXYZDataPoint++;
//			else
//				curXYZDataPoint = 0;
//	}
	public static void checkDataVariance(int[] theX, int[] theY, int[] theZ)
	{
		/* Data Analysis */
		if( Math.abs(theX[curAPRDataPoint]-avgAzimuth) > oriThreshold || Math.abs(theY[curAPRDataPoint]-avgPitch) > oriThreshold || Math.abs(theZ[curAPRDataPoint]-avgRoll) > oriThreshold )
			Log.d("Orientation", "Fall detected: Azimuth=" + theX[curAPRDataPoint] + " avgAzimuth=" + avgAzimuth + " Pitch=" + theY[curAPRDataPoint] + " avgPitch=" + avgPitch + " Roll=" + theZ[curAPRDataPoint] + " avgRoll= " + avgRoll );
		
		/* Data Recording */
		avgAzimuth 	= calcAverage( theX );
		avgPitch 	= calcAverage( theY );
		avgRoll		= calcAverage( theZ );
		
		if( curAPRDataPoint < numDataPoints-1 )
			curAPRDataPoint++;
		else
			curAPRDataPoint = 0;
	}
	
	
	
	private static float calcAverage(float[] theArray) 
	{
		float	mean 	= 0;
		float	sum		= 0;
		
		/* calculate mean */
		for( float f : theArray )
			sum += f;
		mean = ( sum / theArray.length );
		
		return mean;
	}
	private static int calcAverage(int[] theArray) 
	{
		int	mean 	= 0;
		int	sum		= 0;
		
		/* calculate mean */
		for( int f : theArray )
			sum += f;
		mean = ( sum / theArray.length );
		
		return mean;
	}
}
