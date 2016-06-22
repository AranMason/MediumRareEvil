package Pieces;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import Game.Game;
import Game.Game.*;
import Game.Utility.Point;
import Game.Map;
/**
 * Created by AngelOfMercy on 14/01/2016.
 */
public abstract class Piece {

    //----------------------------------------------------------------------------------------------
    //Constants and Variables.
    //----------------------------------------------------------------------------------------------
    /**
     * Utility
     */
    private static int ID_COUNTER = 0;
    String TAG = Piece.class.getSimpleName();

    /**
     * Unit Statistics
     * There are the default rules for a unit.
     */
    protected int TOTAL_MOVEMENT = 3;
    protected int ATTACK_RANGE_MIN = 0, ATTACK_RANGE_MAX = 0;
    protected int ATTACK_DAMAGE = 1;
    protected int MAX_HP = 2;

    /**
     * Unit Status
     * Current Condition of the unit
     */
    protected int CURRENT_HP = MAX_HP;
    private Point CURRENT_LOCATION = new Point(0,0);
    private Point SCREEN_LOCATION;
    private Direction UNIT_FACING = Direction.UP;
    private boolean isTouched;
    /**
     * Unit Identifiers
     * Theses are the ways for the unit to be uniquely identified
     */
    private Bitmap bitmap;
    private String name;
    private int UNIT_ID;
    private int OWNER_ID;

    //----------------------------------------------------------------------------------------------
    //Constructors
    //----------------------------------------------------------------------------------------------
    public Piece(String name, int OWNER_ID, Bitmap bitmap, Point origin){
        this.name = name;
        this.bitmap = bitmap;
        this.UNIT_ID = ID_COUNTER++;
        this.OWNER_ID = OWNER_ID;
        this.SCREEN_LOCATION = origin;
    }

    //----------------------------------------------------------------------------------------------
    //Methods.
    //----------------------------------------------------------------------------------------------

    /**
     *Set the units max hp, and changes the current hp to have the same total 'damage' inflicted on the unit.
     * @param hp A natural number greater to or equal to 1.
     */
    protected void setDefaultHP(int hp){
        if(hp < 1)
            throw new IllegalArgumentException("Max HP cannot be set to less than 1.");
        int difference = MAX_HP - CURRENT_HP;
        this.MAX_HP = 2;
        CURRENT_HP = MAX_HP - difference;
    }

    /**
     * Executes an attack against a target.
     * Determines if the target is legal and deals the appropriate amount of damage.
     *
     * Does not determine if the target is in range.
     * Does not move the unit into the square if the target is killed.
     * @param target
     * @return returns true, if the unit is killed.
     */
    public boolean attackTarget(Piece target, Map m){
        return target.dealDamage(this.ATTACK_DAMAGE);
    }

    /**
     * Inflicts an amount of damage on this piece.
     *
     * @param dmg The amount of damage inflicted. If the value is negative, it will instead apply as healing.
     *            This cannot cause a unit ti go above its max HP.
     * @return True if the damage is lethal. Brings the piece to 0 or less HP.
     */
    public boolean dealDamage(int dmg){
        this.CURRENT_HP = this.CURRENT_HP - dmg;
        this.CURRENT_HP = Math.min(this.MAX_HP, this.CURRENT_HP);
        return CURRENT_HP <= 0;
    }
    //----------------------------------------------------------------------------------------------
    //Getters and Setters.
    //----------------------------------------------------------------------------------------------

    /**
     * Get the damage for this units' attacks.
     * @return
     */
    public int getAttackDamage(){
        return ATTACK_DAMAGE;
    }

    /**
     * Get the total movement for this unit.
     * @return
     */
    public int getTotalMovement(){
        return TOTAL_MOVEMENT;
    }

    /**
     * Get the minimum distance of the units attack.
     * A value of 0 means that it attacks on tile entry.
     * @return
     */
    public int getAttackRangeMin(){
        return ATTACK_RANGE_MIN;
    }

    /**
     * Get the maximum distance of the units attacks.
     * It cannot have a ranged attack if the minimum attack range is 0.
     * @return
     */
    public int getAttackRangeMax(){
        if(ATTACK_RANGE_MIN == 0)
            return 0;
        return ATTACK_RANGE_MAX;
    }

    /**
     * Returns the units name
     * @retrun
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the units unique identifier
     */
    public int getUnitID(){
        return UNIT_ID;
    }

    /**
     * Returns the Unique Identifier for the player that controls the unit.
     * @return
     */
    public int getOwnerID(){
        return OWNER_ID;
    }

    public Point getLocation(){
        return CURRENT_LOCATION;
    }

    public boolean setLocation(Point p, Point origin){
        CURRENT_LOCATION = p;
        SCREEN_LOCATION = setScreenLocation(origin);
        return true;
    }

    /**calculate pixel position based on origin and piece coordinates*/
    private Point setScreenLocation(Point origin){
        /*algorithm: measure along one axis the distance in pixels, then add modifier for
        distance along the opposite axis due to the diagonal nature of the game view.
        Do the same for the other axis.*/
        Float x = origin.getX()+(CURRENT_LOCATION.getX()*-105)+(CURRENT_LOCATION.getY()*-63);
        Float y = origin.getY()+(CURRENT_LOCATION.getY()*59)+(CURRENT_LOCATION.getX()*-33);
        Point xy = new Point(x, y);

        return xy;
    }


    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, SCREEN_LOCATION.x,
                SCREEN_LOCATION.y, null);
    }

}
