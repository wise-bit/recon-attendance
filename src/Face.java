/**
 * This is the face object class, used to declare faces when comparing and storing
 */

public class Face {

    // All of the important features of a face in our scenario

    private int headtoEye = 0;
    private int eyetoMouth = 0;
    private int headtoMouth = 0;
    private int headMouthRatio = 0;

    /**
     * Constructor
     * @param headtoEye
     * @param eyetoMouth
     * @param headtoMouth
     * @param headMouthRatio
     */
    public Face(int headtoEye, int eyetoMouth, int headtoMouth, int headMouthRatio) {
        this.headtoEye = headtoEye;
        this.eyetoMouth = eyetoMouth;
        this.headtoMouth = headtoMouth;
        this.headMouthRatio = headMouthRatio;
    }

    // Getters and setters

    public int getHeadtoEye() {
        return headtoEye;
    }

    public void setHeadtoEye(int headtoEye) {
        this.headtoEye = headtoEye;
    }

    public int getEyetoMouth() {
        return eyetoMouth;
    }

    public void setEyetoMouth(int eyetoMouth) {
        this.eyetoMouth = eyetoMouth;
    }

    public int getHeadtoMouth() {
        return headtoMouth;
    }

    public void setHeadtoMouth(int headtoMouth) {
        this.headtoMouth = headtoMouth;
    }

    public int getHeadMouthRatio() {
        return headMouthRatio;
    }

    public void setHeadMouthRatio(int headMouthRatio) {
        this.headMouthRatio = headMouthRatio;
    }

    // Helper methods

    // Uses a comparator to compare the faces
    /**
     * @param face2
     * @return boolean
     */
    public int compareTo(Face face2) {
        int headtoEyeDifference = Math.abs(face2.headtoEye - this.headtoEye);
        int eyetoMouthDifference = Math.abs(face2.eyetoMouth - this.eyetoMouth);
        int headtoMouthDifference = Math.abs(face2.headtoMouth - this.headtoMouth);
        int headMouthRatioDifference = Math.abs(face2.headMouthRatio - this.headMouthRatio);

        // gets the average of the four values
        return (headtoEyeDifference + eyetoMouthDifference + headtoMouthDifference + headMouthRatioDifference)/4;

    }

}
