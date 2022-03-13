//TO-DO:
// - Input cantus firmus - CHECK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// - Generate ultimate and penultimate notes - CHECK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// - Generate starting note (perfect consonance) - CHECK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// - Fill in space from mm.2 to mm.n-2 (mm.n = ultNote, mm.n-1 = penultNote) - CHECK~~~~~~~~~~~
//              * subproblems
//                  - how far should i forecast lines? at all?
//                  - must compare how cantus firmus notes are approached
import java.util.Random;

class FSCounterpoint
{
    public static void main(String[] args)
    {
        String[] fullScale = {
            "An0","A#0","Bn0","Cn1","C#1","Dn1","D#1","En1","Fn1","F#1","Gn1","G#1",
            "An1","A#1","Bn1","Cn2","C#2","Dn2","D#2","En2","Fn2","F#2","Gn2","G#2",
            "An2","A#2","Bn2","Cn3","C#3","Dn3","D#3","En3","Fn3","F#3","Gn3","G#3",
            "An3","A#3","Bn3","Cn4","C#4","Dn4","D#4","En4","Fn4","F#4","Gn4","G#4",
            "An4","A#4","Bn4","Cn5","C#5","Dn5","D#5","En5","Fn5","F#5","Gn5","G#5",
            "An5","A#5","Bn5","Cn6","C#6","Dn6","D#6","En6","Fn6","F#6","Gn6","G#6"
        };

        String[] morleyCF = {"_Cn3", "+Fn3", "-En3", "-Dn3", "-Cn3", "+Fn3", "+Gn3", "+An3", "-Gn3", "-En3", "-Dn3", "-Cn3_"};
        String[] haydnCF = {"_An2", "+Cn3", "-Bn2", "+En3", "-Cn3", "+Fn3", "-Dn3", "+En3", "-Cn3", "-Bn2", "-An2_"};
        String[] kimbergerCF = {"_Cn3", "+Gn3", "-En3", "+An3", "-Gn3", "-Fn3", "-En3", "-Dn3", "-Cn3_"};
        String[] albrechtsbergerCF = {"_Cn4", "+En4", "-Dn3", "-Bn3", "+Cn4", "-An3", "+Bn3", "-Gn3", "+Cn4", "+En4", "-Dn4", "-Cn4_"};
        String[] cherubiniCF = {"_Dn4", "+Gn4", "-En4", "+Fn4", "-En4", "+An4", "-Gn4", "-En4", "-Cn4", "+Dn4", "+En4", "+Fn4", "-En4", "-Dn3_"};
        String[] jeppesenCF = {"_En4", "-Dn4", "+En4", "+Fn4", "+Gn4", "+An4", "-Dn4", "+Fn4", "-En4_"};

        String[] cantusfirmus0 = populateCantusFirmus(morleyCF);
        String[] counterpoint0 = genCounterpoint(cantusfirmus0, fullScale);

        System.out.println("\n\nTrial 0 (Morley)");
        printCF_CP(cantusfirmus0, counterpoint0);


        String[] cantusfirmus1 = populateCantusFirmus(haydnCF);
        String[] counterpoint1 = genCounterpoint(cantusfirmus1, fullScale);

        System.out.println("Trial 1 (Haydn)");
        printCF_CP(cantusfirmus1, counterpoint1);
        

        String[] cantusfirmus2 = populateCantusFirmus(kimbergerCF);
        String[] counterpoint2 = genCounterpoint(cantusfirmus2, fullScale);

        System.out.println("Trial 2 (Kimberger)");
        printCF_CP(cantusfirmus2, counterpoint2);


        String[] cantusfirmus3 = populateCantusFirmus(albrechtsbergerCF);
        String[] counterpoint3 = genCounterpoint(cantusfirmus3, fullScale);

        System.out.println("Trial 3 (Albrechtsberger)");
        printCF_CP(cantusfirmus3, counterpoint3);

        String[] cantusfirmus4 = populateCantusFirmus(cherubiniCF);
        String[] counterpoint4 = genCounterpoint(cantusfirmus4, fullScale);

        System.out.println("Trial 4 (Cherubini)");
        printCF_CP(cantusfirmus4, counterpoint4);

        String[] cantusfirmus5 = populateCantusFirmus(jeppesenCF);
        String[] counterpoint5 = genCounterpoint(cantusfirmus5, fullScale);

        System.out.println("Trial 5 (Jeppesen)");
        printCF_CP(cantusfirmus5, counterpoint5);


    }




    private static String[] populateCantusFirmus(String[] selectedCF)
    {
        //default morley
        // "_X" indicates X is the starting note, "+X" indicates X was approached by a leap up, "-X" X was approached by step/leap down
        //" (+/-) X_" indicates X is the ending note of the cantus firmus
        // Xn read "X natural", X# read "X sharp", Xf read "X flat"


        ///MIGHT be better to use linked lists for this
        //need to cleave off excess size- ex morley is 12 notes long and cherubini is 14 notes long. 15 as a size, then waste 3 and 1
        //LL would mitigate this waste of storage space
        

        //String[] selectedCF = jeppesenCF;
        String[] cf = new String[selectedCF.length];

        //System.out.println("Cantus firmus populating");
        for (int i =0; i < selectedCF.length; i++)
        {
            cf[i] = selectedCF[i];
            //System.out.println(i + ".) " + cf[i]);
        }

        return cf;
    }





    //Parent function for all counterpoint generation master program flow
    private static String[] genCounterpoint(String[] cf, String[] scale)
    {
        String cf_ultNote = cf[cf.length-1];
        //string builder so that strings are mutable. this is particularly helpful when editing the octave
        StringBuilder cp_ultNote = genCounterpoint_ultNote(cf_ultNote);
        //System.out.println();System.out.println("Counterpoint final note: "); System.out.println(cp_ultNote);

        
        String cf_penultNote = cf[cf.length-2];
        String cp_penultNote = genCounterpoint_penultNote(cf_penultNote, cf_ultNote, scale, cp_ultNote);
        //System.out.println();System.out.println("Counterpoint penultimate note: "); System.out.println(cp_penultNote);


        String cf_startNote = cf[0];
        //returns an item of the scale array
        String cp_startNote = genCounterpoint_startNote(cf_startNote, scale);
        //System.out.println();System.out.println("Counterpoint start note: "); System.out.println(cp_startNote);

        String[] counterpoint = genCounterpoint_body(cf, scale, cp_ultNote, cp_penultNote, cp_startNote);
        //System.out.println();System.out.println();System.out.println("Counterpoint Selection: ");
        //System.out.println("Note pool (Should be imperfect intervals above cantus firmus): " + cp_Body[0] + " " + cp_Body[1] + " " + cp_Body[3]);//" " + cp_Body[2] + " " + cp_Body[4]);
        for (int i = 0; i < counterpoint.length; i++)
        {
            //System.out.println(i + ".) " + counterpoint[i]);
        }

        //System.out.println("Counterpoint penultimate note: " + cp_penultNote);
        //System.out.println("Counterpoint ultimate note: "+cp_ultNote);// System.out.print(cp_ultNote);

        return counterpoint;       
    }
    




    //Child function for determining ultNote for counterpoint - not the most efficient or smartest design but it works
    private static StringBuilder genCounterpoint_ultNote(String cf_ultNote)
    {
        //increasing the cp's last note by an octave, returning and using for cp_ultNote
        StringBuilder cp_ultNote = new StringBuilder(cf_ultNote);
        char cf_tonicOctave = cf_ultNote.charAt(3);

        if (cf_tonicOctave == '1')
        {
            cp_ultNote.setCharAt(3, '2');
        }
            
        else if (cf_tonicOctave == '2')
        {
            cp_ultNote.setCharAt(3, '3');
        }
            

        else if (cf_tonicOctave == '3')
        {
            cp_ultNote.setCharAt(3, '4');

        }

        else if (cf_tonicOctave == '4')
        {
            cp_ultNote.setCharAt(3, '5');
        }

        else if (cf_tonicOctave == '5')
        {
            cp_ultNote.setCharAt(3, '6');
        }

        else
        {
            //else return the unision (not necessary but just a safety precaution)
            cp_ultNote.setCharAt(3, cf_tonicOctave);
        }

        if (cp_ultNote.charAt(0) == '-')
        {
            cp_ultNote.setCharAt(0, '+');
        }
        else if (cp_ultNote.charAt(0) == '+')
        {
            cp_ultNote.setCharAt(0, '-');
        }


        return cp_ultNote;
    }




    //Must form contrary stepwise motion with the cantus firmus from the penult to the ult mm
    private static String genCounterpoint_penultNote(String cf_penultNote, String cf_ultNote, String[] scale, StringBuilder cp_ultNote)
    {
        //dumbass conversion step to avoid an array search in the creation of cp_ultNote
        String cp_ultNotestr = cp_ultNote.toString();
        cp_ultNotestr = cp_ultNote.substring(1,4);


        //forming contrary stepwise motion
        if (cf_ultNote.charAt(0) == '-')
        {
            //if cf ult note approached by step/leap down, make the cp penultNote one note below the tonic. 
            //the identifier that indicates whether a note was approached from above or below will be missing from penult note until remainder is filled
            //cp_penultNote += '+'; <-- cannot know this until the measure before the penultimate note is complete (LATER)

            //find note (subtonic)
            for (int i = 0; i < scale.length; i++)
            {
                if (scale[i].equals(cp_ultNotestr))
                {
                    //penultimate note is a halfstep below ultimate note in counterpoint
                    //SHOULD POTENTIALLY RETURN I-2 AS WELL - FOR THE FLAT 7
                    return scale[i-1];
                }
            }
        }


        else if (cf_ultNote.charAt(0) == '+')
        {
            //find note (supertonic)
            for (int i = 0; i < scale.length; i++)
            {
                if (scale[i].equals(cp_ultNotestr))
                {
                    return scale[i+2];
                }
            }
        }


        return "Stuff is jank in the gen_penultNote function";
        

    }

    private static String genCounterpoint_startNote(String cf_startNote, String[] scale)
    {
        cf_startNote = cf_startNote.substring(1,4);
        for (int i = 0; i < scale.length; i++)
        {
            if (scale[i].equals(cf_startNote))
            {
                //randomizer for either perfect octave (0) or perfect fifth (1) - the unison is excluded
                Random r = new Random();
                int pConsonanceDecider = r.nextInt(2);
                if (pConsonanceDecider == 0)
                {
                    return scale[i+12];
                }
                else if (pConsonanceDecider == 1)
                {
                    return scale[i+7];
                }
            }
        }
        return "Stuff is jank in the gen_startNote function";
    }

    private static String[] genCounterpoint_body(String[] cf, String[] scale, StringBuilder cp_ultNote, String cp_penultNote, String cp_startNote)
    {

        //subtracted 3 for the predetermined notes - the body will be concatenated with the counterpoint after this function
        String[] counterpoint_Body = new String[cf.length];
        String[] notePool = new String[15];
        counterpoint_Body[0] = cp_startNote;
        
        //this is gonna be sexy as hell when it works -- it works
        for (int i = 0; i<cf.length-3; i++)
        {
            
            
            String cf_Note = cf[i];
            String cp_Note = counterpoint_Body[i];
            int currentInterval = genCounterpointBody_intervalTracking(scale, cf, cp_Note, cf_Note);
            String cf_nextNote = cf[i+1];
            String cf_nextnextNote = cf[i+2];
            //cf_nextNote = cf_nextNote.substring(1,4);
            notePool = genCounterpointBody_noteSelectionPool(scale, cf, currentInterval, cf_nextNote, cf_nextnextNote);

            String[] notePool_PRUNED = new String[6];
            int prunedIndex = 0;
            for (int j = 0; j < notePool.length; j++)
            {
                if (notePool[j] != null)
                {
                    notePool_PRUNED[prunedIndex] = notePool[j];
                    prunedIndex++;
                } 
                //System.out.println("index "+ j + ":  " +notePool[j]);
            }
            Random r = new Random();
            int cp_Note_decider = r.nextInt(prunedIndex);
            
            counterpoint_Body[i+1] = notePool_PRUNED[cp_Note_decider];

        }
        //find interval between current note and cantus firmus
        //first time will always be with the start note

        //notePool = genCounterpointBody_noteVerification(scale, cf, currentInterval, cf_nextNote, cp_startNote, notePool);
        String cp_ultNoteStr = cp_ultNote.toString();
        counterpoint_Body[cf.length-1] = cp_ultNoteStr;
        counterpoint_Body[cf.length-2] = cp_penultNote;


        return counterpoint_Body;



    }


    private static int genCounterpointBody_intervalTracking(String[] scale, String[] cf, String cp_Note, String cf_Note)
    {
        cf_Note = cf_Note.substring(1,4);
    

        for (int i = 0; i<scale.length; i++)
        {

            if (scale[i].equals(cf_Note))
            {
                int k = i;
                int intervalTracker = 0;
                //System.out.println(scale[k]);
                //System.out.println(cp_Note);
                while(scale[k] != cp_Note)
                {
                    k++;
                    intervalTracker++;
                }

                return intervalTracker;
            }
        }
        return -1;
    }

    private static String[] genCounterpointBody_noteSelectionPool(String[] scale, String[] cf, int currentInterval, String cf_nextNote, String cf_nextnextNote)
    {

        //need to change the note pool structure
        //NOTEPOOL V2.0
        //Notepool is an array of length 15, organized in the following manner:
        //ID:        0    1    2    3    4    5    6    7    8    9    10   11   12   13    14
        //CON:      +m3, +M3, +P4, +P5, +m6, +M6, +P8, -m3, -M3, -P4, -P5, -m6, -M6, -P8, unison


        String[] notePool = new String[15];   //  <-- how it should be implemented, this is just a test

        //make a copy of cf_nextNote as a stringbuilder for use of the charAt function later
        StringBuilder cf_nextNoteSBcon = new StringBuilder(cf_nextNote);
        int i = 0;
        //if the current interval is perfect, choose from a pool of imperfect intervals
        if (currentInterval == 7 || currentInterval == 12)
        {
            //notePool = new String[4];
            
            //need not equals to function needed
            String cf_nextNoteTrimmed = cf_nextNote.substring(1,4);
            while (!scale[i].equals(cf_nextNoteTrimmed))
            {
                i++;
            }
            char cf_Movement = cf_nextNoteSBcon.charAt(0);
            if (cf_Movement == '+')
            {
                //notePool = new String[2];
                //String maj3 = scale[i+4]; //<-- major 3rd ~above~ cantus firmus next note
                //String maj6 = scale[i+9]; //<-- major 6th ~above~ cantus firmus next note
                
                //Not necessary for this particular piece, but helpful in another im sure
                //if it is harmonically allowed, oblique motion is okay for the note selection pool
                //int projectedObliqueInterval = genCounterpointBody_intervalTracking(scale, cf, cp_Note, cf_nextNote);
                //if (projectedObliqueInterval == 4 || projectedObliqueInterval == 9)
                /*{
                    //String[] notePool = new String[3];
                    notePool[1] = scale[i+4];//<-- major 3rd ~above~ cantus firmus next note
                    notePool[5] = scale[i+9];//<-- major 6th ~above~ cantus firmus next note
                    notePool[14] = cp_Note; //<-- No move
                    //return notePool;
                }
                */
            
                
                    //String[] notePool = new String[2];
                    notePool[1] = scale[i+4]; //<-- major 3rd ~above~ cantus firmus next note
                    notePool[5] = scale[i+9]; //<-- major 6th ~above~ cantus firmus next note
                    //return notePool;
                

                
                if (genCounterpointBody_isCrossVoicing(scale[i-3], cf_nextNote, cf_nextnextNote, scale) == false)
                {
                    notePool[7] = scale[i-3]; // <-- minor 3rd ~below~ cantus firmus note
                }
                if (genCounterpointBody_isCrossVoicing(scale[i-8], cf_nextNote, cf_nextnextNote, scale) == false)
                {
                    notePool[11] = scale[i-8]; // <-- minor 6th ~below~ cantus firmus note
                }
                
                
            }
            else if (cf_Movement == '-')
            {
                if (genCounterpointBody_intervalTracking(scale, cf, scale[i+4], cf_nextNote) < 13)
                {
                    notePool[1] = scale[i+4];
                }

                if (genCounterpointBody_intervalTracking(scale, cf, scale[i+9], cf_nextNote) < 13)
                {
                    notePool[5] = scale[i+9];
                }
                
            }
            

            

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //NEED TO FIGURE OUT WHEN IT IS OKAY TO MOVE DOWNWARDS -BIG DEAL
            // doing that w isCrossvoicing function above
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        else
        {

            cf_nextNote = cf_nextNote.substring(1,4);
            while (!scale[i].equals(cf_nextNote))
            {
                i++;
            }

            //String[] notePool = new String[4];

            notePool[1] = scale[i+4]; //<-- major 3rd above cantus firmus next note
            notePool[5] = scale[i+9]; //<-- major 6th above cantus firmus next note
            notePool[6] = scale[i+7]; //<-- perfect fifth cantus firmus next note (must be approached by contrary motion from an imperfect consonance)
            notePool[3] = scale[i+12];//<-- perfect octave above cantus firmus next note
        }
        



        return notePool;
    }


    private static Boolean genCounterpointBody_isCrossVoicing(String cp_Note, String cf_nextNote, String cf_nextnextNote, String[] scale)
    {
        //FUNCTION checks between next two cf notes, and below cf notes the determine if the counterpoint is a crossvoicing

        //need interval between cf_nextNote and cf_nextnextNote
        //must check that the next cp_Note (cp_Note) is not within this range - this would be a cross voicing
        int i = 0;
        //StringBuilder cf_nextNoteSBcon = new StringBuilder(cf_nextNote);
        StringBuilder cf_nextnextNoteSBcon = new StringBuilder(cf_nextnextNote);

        //if the cantus firmus moves upwards twice, the chance of a cross voicing is higher and also demands the indeces be sought in ascending order
        //i.e. iterating through the scale to the cf_nextNote, then to the cf_nextnextNote
        
        String cf_nextNoteTrimmed = cf_nextNote.substring(1,4);
        while (!scale[i].equals(cf_nextNoteTrimmed))
        {
            i++;
        }
        int k = i;
        String cf_nextnextNoteTrimmed = cf_nextnextNote.substring(1,4);


        //scale[i] == cf_nextNote - i is its index on scale
        //scale[k] == cf_nextnextNote - k is its index on scale
        //cp_Note is the prospective counterpoint note

        //within this range, if the counterpoint note is detected then it is deemed a cross voicing and discounted from the note pool
        if (cf_nextnextNoteSBcon.charAt(0) == '+')
        {
            while (!scale[k].equals(cf_nextnextNoteTrimmed))
            {
                k++;
            }
            //scale[i] is cf_nextNote
            int j = i;
            while (!scale[j].equals(scale[k]))
            {
                if (scale[j].equals(cp_Note))
                {
                    return true;
                }
                j++;
            }
        }
        else if (cf_nextnextNoteSBcon.charAt(0) == '-')
        {
            while (!scale[k].equals(cf_nextnextNoteTrimmed))
            {
                k--;
            }
            //scale[k] is cf_nextnextNote
            int j = k;
            while (!scale[j].equals(scale[i]))
            {
                if (scale[j].equals(cp_Note))
                {
                    return true;
                }
                j++;
            }
        }

        //if the prospective cp note is outside of the range, and is lower than the cantus firmus, it must be discarded as well
        int cp_noteIndex = 0;
        while (!scale[cp_noteIndex].equals(cp_Note))
        {
            cp_noteIndex++;
        }
        if (cp_noteIndex < i || cp_noteIndex < k)
        {
            return true;
        }

        return false;
    }
 

    private static void printCF_CP(String[] cf, String[] cp)
    {
        for (int i = 0; i<cf.length; i++)
        {
            System.out.println("Cantus firmus: \t\t Counterpoint: \n " + cf[i] + "              \t\t  " + cp[i]);
        }
        System.out.println("\n");
    }
}
