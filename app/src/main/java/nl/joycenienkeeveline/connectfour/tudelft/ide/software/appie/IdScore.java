package nl.joycenienkeeveline.connectfour.tudelft.ide.software.appie;

//This class is used to store the id and score in IdScore, so IdScore can be used in the playermap
// in the GameOver class and the RankingPage class.
public class IdScore <X, Y> {
        public final X id;
        public final Y score;
        public IdScore(X id, Y score) {
            this.id = id;
            this.score = score;
        }

}
