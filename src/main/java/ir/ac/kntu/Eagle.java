package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Eagle extends ImageView {

    private ImageView imgView;



    public Eagle(int x, int y) {
        setImgView(new ImageView(new Image("images/Eagle.png")));
        getImgView().setLayoutX(x);
        getImgView().setLayoutY(y);
    }

    public ImageView getImgView() {
        return imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }

}