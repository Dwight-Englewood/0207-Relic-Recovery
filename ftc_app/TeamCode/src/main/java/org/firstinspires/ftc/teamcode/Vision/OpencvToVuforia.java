/*public Mat readFrame() {
         VuforiaLocalizer.CloseableFrame frame;
         Image rgb = null;

         try {
             // grab the last frame pushed onto the queue
             frame = vuforia.getFrameQueue().take();
         } catch (InterruptedException e) {
             Log.d(LOG_TAG, "Problem taking frame off Vuforia queue");
             e.printStackTrace();
             return null;
         }

         // basically get the number of formats for this frame
         long numImages = frame.getNumImages();

         // set rgb object if one of the formats is RGB565
         for(int i = 0; i < numImages; i++) {
             if(frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                 rgb = frame.getImage(i);
                 break;
             }
         }

         if(rgb == null) {
             Log.d(LOG_TAG, "Image format not found");
             return null;
         }

         // create a new bitmap and copy the byte buffer returned by rgb.getPixels() to it
         Bitmap bm = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
         bm.copyPixelsFromBuffer(rgb.getPixels());

         // construct an OpenCV mat from the bitmap using Utils.bitmapToMat()
         Mat mat = new Mat(bm.getWidth(), bm.getHeight(), CvType.CV_8UC4);
         Utils.bitmapToMat(bm, mat);

         // convert to BGR before returning
         Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR);

         frame.close();

         Log.d(LOG_TAG, "Frame closed");

         return mat;
     }
     */