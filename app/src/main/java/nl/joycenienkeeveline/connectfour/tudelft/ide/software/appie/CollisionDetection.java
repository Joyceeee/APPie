package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;


import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

public class CollisionDetection {

    private ImageView umbrella;

    //Check for collision
    //https://stackoverflow.com/questions/24600378/how-to-detect-when-a-imageview-is-in-collision-with-another-imageview
    //https://laaptu.wordpress.com/2013/12/12/android-view-basics-coordinatesmarginpaddingdippx/
    boolean viewsOverlap(View v1, View v2, View checkumbrella) {
        //Determine position of image v1 and set a rectangle around it
        int[] v1_coords = new int[2];
        v1.getLocationOnScreen(v1_coords);
        int v1_w = v1.getWidth();
        int v1_h = v1.getHeight();
        Rect v1_rect;
        Rect v4_rect;

        //Check whether picture is umbrella (needs 2 rectangles because of dimensions)
        if(v1==umbrella){
            //Rectangle sheet
            v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10),v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] +(v1_h/2)-(v1_h/10));
            //Rectangle handle
            v4_rect = new Rect(v1_coords[0]+(v1_w/10)+(v1_w/3), v1_coords[1]+(v1_h/10)+(v1_h/2),v1_coords[0] + v1_w-(v1_w/10)-(v1_w/3), v1_coords[1] + v1_h);
        }
        else{
            v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10),v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] + v1_h-(v1_h/10));
            v4_rect = new Rect(0, 0, 1, 1);
        }

        //Determine position of image v2 and set a rectangle around it
        int[] v2_coords = new int[2];
        v2.getLocationOnScreen(v2_coords);
        int v2_w = v2.getWidth();
        int v2_h = v2.getHeight();

        //Separating the bitmoji into multiple rectangles for the head and the body by adjusting the boundaries
        //Rectangle body
        Rect v2_rect = new Rect(v2_coords[0], v2_coords[1]+(v2_h/7*6), v2_coords[0] + v2_w, v2_coords[1] + v2_h);
        //Rectangle head
        Rect v3_rect = new Rect(v2_coords[0]+(v2_w/3), v2_coords[1], v2_coords[0] + v2_w-(v2_w/3), v2_coords[1] + v2_h-(v2_h/7));

        //Check whether collision
        if ((v1_rect.intersect(v2_rect) || v1_rect.contains(v2_rect) || v2_rect.contains(v1_rect))
                ||
                (v1_rect.intersect(v3_rect) || v1_rect.contains(v3_rect) || v3_rect.contains(v1_rect))
                ||
                (v4_rect.intersect(v2_rect) || v4_rect.contains(v2_rect) || v2_rect.contains(v4_rect))
                ||
                (v4_rect.intersect(v3_rect) || v4_rect.contains(v3_rect) || v3_rect.contains(v4_rect))
                ){
            return  true;
        }

        //Check whether the umbrella covering is active or not to see if
        // extra collision detection is needed
        else if (checkumbrella.getVisibility()==View.VISIBLE){
            return (viewsOverlapUmbrella(v1,checkumbrella));
        }
        else return false;
    }


    //Check for collision with umbrella and falling objects
    private boolean viewsOverlapUmbrella(View v1,View checkumbrellacover) {
        //Determine position of image v1 and set a rectangle around it
        int[] v1_coords = new int[2];
        v1.getLocationOnScreen(v1_coords);
        int v1_w = v1.getWidth();
        int v1_h = v1.getHeight();
        Rect v1_rect;
        Rect v4_rect;

        //Check whether picture is umbrella (needs 2 rectangles because of dimensions)
        if(v1==umbrella){
            //Rectangle sheet
            v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10),v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] +(v1_h/2)-(v1_h/10));
            //Rectangle handle
            v4_rect = new Rect(v1_coords[0]+(v1_w/10)+(v1_w/3), v1_coords[1]+(v1_h/10)+(v1_h/2),v1_coords[0] + v1_w-(v1_w/10)-(v1_w/3), v1_coords[1] + v1_h);
        }
        else{
            v1_rect = new Rect(v1_coords[0]+(v1_w/10), v1_coords[1]+(v1_h/10),v1_coords[0] + v1_w-(v1_w/10), v1_coords[1] + v1_h-(v1_h/10));
            v4_rect = new Rect(0, 0, 1, 1);
        }

        //Determine position of image v2 and set a rectangle around it
        int[] v2_coords = new int[2];
        checkumbrellacover.getLocationOnScreen(v2_coords);
        int v2_w = checkumbrellacover.getWidth();
        int v2_h = checkumbrellacover.getHeight();

        //Separating the umbrella into multiple rectangles as done already above
        //Rectangle sheet
        Rect v2_rect = new Rect(v2_coords[0]+(v2_w/10), v2_coords[1]+(v2_h/10),v2_coords[0] + v2_w-(v2_w/10), v2_coords[1] + v2_h-(v2_h/10)+(v2_h/2));

        //Rectangle handle
        Rect v3_rect = new Rect(v2_coords[0]+(v2_w/10)+(v2_w/3), v2_coords[1]+(v2_h/10)+(v2_h/2),v2_coords[0] + v2_w-(v2_w/10)-(v2_w/3), v2_coords[1] + v2_h);

        //Check whether collision
        if ((v1_rect.intersect(v2_rect) || v1_rect.contains(v2_rect) || v2_rect.contains(v1_rect))
                ||
                (v1_rect.intersect(v3_rect) || v1_rect.contains(v3_rect) || v3_rect.contains(v1_rect))
                ||
                (v4_rect.intersect(v2_rect) || v4_rect.contains(v2_rect) || v2_rect.contains(v4_rect))
                ||
                (v4_rect.intersect(v3_rect) || v4_rect.contains(v3_rect) || v3_rect.contains(v4_rect))
                ){
            return  true;
        }
        else return false;
    }

}