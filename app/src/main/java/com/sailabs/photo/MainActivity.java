package com.sailabs.photo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends Activity implements SurfaceHolder.Callback
{
	  private Camera camera = null;
	  private SurfaceView cameraSurfaceView = null;
	  private SurfaceHolder cameraSurfaceHolder = null;
	  private boolean previewing = false;
	  RelativeLayout relativeLayout;
	  private Context myContext;
	  private boolean cameraFront = false;
	  public int glo = 0;
   // private TextView text = null;

	  private Button capture = null;
	 
	  @Override
	  protected void onCreate(Bundle savedInstanceState) 
	  {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
		  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		  myContext = this;
		  getWindow().setFormat(PixelFormat.TRANSLUCENT);
	      requestWindowFeature(Window.FEATURE_NO_TITLE);
	      getWindow().setFlags(
                  WindowManager.LayoutParams.FLAG_FULLSCREEN,
                  WindowManager.LayoutParams.FLAG_FULLSCREEN);
	          
	    setContentView(R.layout.activity_main);
	     
	    relativeLayout=(RelativeLayout) findViewById(R.id.containerImg);
	    relativeLayout.setDrawingCacheEnabled(true);
	    cameraSurfaceView = (SurfaceView)
	                                       findViewById(R.id.surfaceView1);
        //  text = (TextView) findViewById(R.id.textView);
	  //  cameraSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(640, 480));
	    cameraSurfaceHolder = cameraSurfaceView.getHolder();
	    cameraSurfaceHolder.addCallback(this);
	//    cameraSurfaceHolder.setType(SurfaceHolder.
	  //                                               SURFACE_TYPE_PUSH_BUFFERS);
	          
	
	     
	   
	    capture = (Button)findViewById(R.id.button1);
	    capture.setOnClickListener(new OnClickListener()
	    {   
	      @Override
	      public void onClick(View v) 
	      {

              Toast.makeText(getApplicationContext(), "1",
                      Toast.LENGTH_LONG).show();
              try{
                  Thread.sleep(1000);

              }catch (Exception e)
              {
                  e.printStackTrace();
              }
              Toast.makeText(getApplicationContext(), "2",
                      Toast.LENGTH_LONG).show();
              try{
                  Thread.sleep(1000);

              }catch (Exception e)
              {
                  e.printStackTrace();
              }
              Toast.makeText(getApplicationContext(), "3",
                      Toast.LENGTH_LONG).show();
              try{
                  Thread.sleep(1000);

              }catch (Exception e)
              {
                  e.printStackTrace();
              }


              CaptureThread captureThread = new CaptureThread();
			  captureThread.start();


			  // TODO Auto-generated method stub
    }
	    });
	  }
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}


	ShutterCallback cameraShutterCallback = new ShutterCallback()
	  {  
	    @Override
	    public void onShutter() 
	    {
	      // TODO Auto-generated method stub   
	    }
	  };
	 
	  PictureCallback cameraPictureCallbackRaw = new PictureCallback() 
	  {  
	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) 
	    {
	      // TODO Auto-generated method stub   
	    }
	  };
	 
	  PictureCallback cameraPictureCallbackJpeg = new PictureCallback()
	  {  
	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) 
	    {
	      // TODO Auto-generated method stub   
	      Bitmap cameraBitmap = BitmapFactory.decodeByteArray
	                                                                  (data, 0, data.length);
	      
	     int   wid = cameraBitmap.getWidth();
	     int  hgt = cameraBitmap.getHeight();
	      
	    //  Toast.makeText(getApplicationContext(), wid+""+hgt, Toast.LENGTH_SHORT).show();
	      Bitmap newImage = Bitmap.createBitmap
	                                        (wid, hgt, Bitmap.Config.ARGB_8888);
	   
	      Canvas canvas = new Canvas(newImage);
	   
	      canvas.drawBitmap(cameraBitmap, 0f, 0f, null);
	   
	      Drawable drawable = getResources().getDrawable(R.drawable.mark3);
	      drawable.setBounds(20, 30, drawable.getIntrinsicWidth()+20, drawable.getIntrinsicHeight()+30);
	      drawable.draw(canvas);
	   
	    	

	      //File storagePath = new File(Environment.getExternalStorageDirectory() + "/pics/");
	     // storagePath.mkdirs();
			File mediaStorageDir = new File("/sdcard/", "pics");
	   
	      // File myImage = new File(storagePath, +".jpg");
			File myImage = new File(mediaStorageDir.getPath() + File.separator + "pic" + glo + ".png");
			Log.d("naval", "File path :" + myImage);
			glo++;
	            
	      try
	      {
	        FileOutputStream out = new FileOutputStream(myImage);
	        newImage.compress(Bitmap.CompressFormat.JPEG, 80, out);


	        out.flush();
	        out.close();
	      }
	      catch(FileNotFoundException e)
	      {
	        Log.d("In Saving File", e + "");    
	      }
	      catch(IOException e)
	      {
	        Log.d("In Saving File", e + "");
	      }
	   
	      camera.startPreview();
	   
	    
	   
	     // newImage.recycle();
	    //  newImage = null;
	   
	     // Intent intent = new Intent();
	    //  intent.setAction(Intent.ACTION_VIEW);
	     
	    //  intent.setDataAndType(Uri.parse("file://" + myImage.getAbsolutePath()), "image/*");
	    //  startActivity(intent);
	      
	    }
	  };
	 
	  @Override
	  public void surfaceChanged(SurfaceHolder holder, 
	                                       int format, int width, int height) 
	  {
	    // TODO Auto-generated method stub
	     
	    if(previewing)
	    {
	      camera.stopPreview();
	      previewing = false;
	    }
	    try 
	    {
	      Camera.Parameters parameters = camera.getParameters();
	     // parameters.setPreviewSize(640, 480);
	     // parameters.setPictureSize(640, 480);
	      if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
	          camera.setDisplayOrientation(90);
	        
	      }
	      
	     // parameters.setRotation(90);
	      camera.setParameters(parameters);

	      camera.setPreviewDisplay(cameraSurfaceHolder);
	      camera.startPreview();
	      previewing = true;
	    } 
	    catch (IOException e) 
	    {
	      // TODO Auto-generated catch block
	      e.printStackTrace();  
	    }
	  }

	  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	  @Override
	  public void surfaceCreated(SurfaceHolder holder) 
	  {
	    // TODO Auto-generated method stub
	    try
	    {
	      camera = Camera.open(findFrontFacingCamera());
	    }
	    catch(RuntimeException e)
	    {
	      Toast.makeText(getApplicationContext(), "Device camera  is not working properly, please try after sometime.", Toast.LENGTH_LONG).show();
	    }
	  }

	  @Override
	  public void surfaceDestroyed(SurfaceHolder holder) 
	  {
	    // TODO Auto-generated method stub
	    camera.stopPreview();
	    camera.release();
	    camera = null;
	    previewing = false;
	  }

	class CaptureThread extends Thread {

		@Override
		public void run() {

			int count = 0;

			while (count < 30) {
				//  mFileName = mLocation + "/pic" + count + ".jpg";
				try {
					Log.d("Naval","camera clicked");
					camera.takePicture(cameraShutterCallback,
	                                       cameraPictureCallbackRaw,
	                                       cameraPictureCallbackJpeg);

					//mCamera.takePicture(null, null, mPicture);

					count++;


					Thread.sleep(1000);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			glo = 0; // images will be save from 0 in pics folder
			try {
				// wait(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			createGif();


		}


		public void createGif() {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			AnimatedGifEncoder encoder = new AnimatedGifEncoder();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 6;

			encoder.setDelay(50);
			encoder.start(bos);
			for (int i = 0; i < 30; i++) {
				Bitmap bMap = BitmapFactory.decodeFile("/storage/emulated/0/pics/pic" + i + ".png", options);
				Log.d("naval", "added image");

				encoder.addFrame(bMap);
				// encoder.setQuality(100);
			}
			encoder.finish();

			writeToFile(bos.toByteArray());

			try {
				Log.d("naval - lets sleep", "Turn again to click");
				Thread.sleep(5000);

				SocketServerThread sc = new SocketServerThread();
				sc.initialize();
				sc.start();


				//capture.performClick();

			} catch (Exception e) {
				e.printStackTrace();
			}



		}

		public void writeToFile(byte[] array) {
			try {
				String path = Environment.getExternalStorageDirectory() + "/gif/gif.gif";
				FileOutputStream stream = new FileOutputStream(path);
				stream.write(array);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class SocketServerThread extends Thread {

		int count = 0;
		String message = "";
		ServerSocket serverSocket = null;
		// String message = "";
		static final int socketServerPORT = 4444;
		public String ip = getIpAddress();

		public SocketServerThread()throws IOException
		{
			// serverSocket

			Log.d("naval", " constructor thread");


		}
		public void initialize () {
			try {
				Log.d("naval", "Initialize socket");

				serverSocket = new ServerSocket();
				serverSocket.setReuseAddress(true);
				serverSocket.bind(new InetSocketAddress(6677));
			}catch (IOException e) {
				e.printStackTrace();

			}
		}

		@Override
		public void run() {
			try {
				// InetAddress inetAddress = InetAddress.getByName("192.168.1.1");


				// serverSocket = new ServerSocket(socketServerPORT);
				if(serverSocket == null)
				{
					Log.d("naval", "ye to null hai bhai");

				}
				while (true) {
					Log.d("naval","waiting for packet at port  and IP :"+ip+"a"+serverSocket.getInetAddress()+"b"+serverSocket.getLocalPort());
					Socket socket = serverSocket.accept();
					Log.d("naval", " packet get");
					String encoding   = "";
					/* receiving from client*/
					try {
						InputStream inputStream = socket.getInputStream();

						BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
						StringBuilder total = new StringBuilder();
						String line;
						while ((line = r.readLine()) != null) {
							total.append(line);
						}
						Log.d("naval", total.toString());
						message = total.toString();
						if(message.equals("start")) {

							try{
								// r.close();
								// inputStream.close();
								serverSocket.close();
								socket.close();

							}catch (Exception e)
							{
								e.printStackTrace();
							}

							MainActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {

									Log.d("naval","click performed");

									capture.performClick();

								}
							});
							//
							Log.d("naval","breaking while loop");
							break;
						}



					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

					//socket.close();


					Log.d("naval","closing socket");
					count++;

					//   message += "#" + count + " from "
					//  + socket.getInetAddress() + ":"
					//  + socket.getPort() + "\n";


				}
				//serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String getIpAddress() {
			String ip = "";
			try {
				Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
						.getNetworkInterfaces();
				while (enumNetworkInterfaces.hasMoreElements()) {
					NetworkInterface networkInterface = enumNetworkInterfaces
							.nextElement();
					Enumeration<InetAddress> enumInetAddress = networkInterface
							.getInetAddresses();
					while (enumInetAddress.hasMoreElements()) {
						InetAddress inetAddress = enumInetAddress
								.nextElement();

						if (inetAddress.isSiteLocalAddress()) {
							ip += "Server running at : "
									+ inetAddress.getHostAddress();
						}
					}
				}

			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ip += "Something Wrong! " + e.toString() + "\n";
			}
			return ip;
		}
	}

}
