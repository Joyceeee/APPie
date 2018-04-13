package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import static android.app.Activity.RESULT_OK;

//Import photo from external storage and crop it to the right dimensions
//Source: https://www.android-examples.com/android-image-cropping-example-tutorial-pick-gallery-camera/
//Source: https://www.youtube.com/watch?v=rYzkv_KuZo4
public class ImageFromGallery {

    //Make link to MainActivity
    private MainActivity mainActivity;
    public ImageFromGallery(MainActivity mainActivity) {this.mainActivity=mainActivity;}

    //Initialise elements to be able to import photos from gallery
    Uri uri;
    Intent GalIntent, CropIntent ;
    public  Bitmap bitmap;


    public void GetImageFromGallery(){
        GalIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mainActivity.startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);
    }


    public void reactionOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageCropFunction();
        }

        else if (requestCode == 2) {
            if (data != null) {
                uri = data.getData();
                ImageCropFunction();
            }
        }
        else if (requestCode == 1) {

            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                //Give the images the wanted picture
                ImageView photoGallery = (ImageView)mainActivity.findViewById(R.id.photogallery);
                ImageView photoGalleryBitmoji = (ImageView)mainActivity.findViewById(R.id.photogallerybitmoji);
                photoGallery.setImageBitmap(bitmap);
                photoGalleryBitmoji.setImageBitmap(bitmap);
            }
        }
    }

    //Crop image to wanted dimensions
    public void ImageCropFunction() {
        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 170);
            CropIntent.putExtra("outputY", 230);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            mainActivity.startActivityForResult(CropIntent, 1);
        } catch (ActivityNotFoundException e) {
        }
    }
    //Stop activity get Image from Gallery
}
