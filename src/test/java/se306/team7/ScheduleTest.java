package se306.team7;

import se306.team7.Digraph.Node;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.List;

public class ScheduleTest {

    @Test
    public void ScheduleToStringList_ReturnsList_WhenScheduleValid () {

        Schedule s = new Schedule(2);
        Node n1 = new Node("1", 2);
        Node n2 = new Node("2", 3);
        Node n3 = new Node("3", 1);
        Node n4 = new Node("4", 5);

        // 1 -> 2
        n1.addLink(n1, "1", n2, 3);
        n2.addLink(n1, "1", n2, 3);

        // 1 -> 3
        n1.addLink(n1, "1", n3, 2);
        n3.addLink(n1, "1", n3, 2);

        // 2 - > 4
        n2.addLink(n2, "2", n4, 2);
        n4.addLink(n2, "2", n4, 2);

        // 3 -> 4
        n3.addLink(n3, "3", n4, 4);
        n4.addLink(n3, "3", n4, 4);

        s.scheduleTask(1, n1);
        s.scheduleTask(1, n2);
        s.scheduleTask(2, n3);
        s.scheduleTask(2, n4);

        List<String> output = s.scheduleToStringList();

        assertEquals(output.size(), 8);
        assertEquals("\t1\t\t[Weight=2,Start=0,Processor=1];", output.get(0));
        assertEquals("\t2\t\t[Weight=3,Start=2,Processor=1];", output.get(1));
        assertEquals("\t1 -> 2\t[Weight=3];", output.get(2));
        assertEquals("\t3\t\t[Weight=1,Start=4,Processor=2];", output.get(3));
        assertEquals("\t1 -> 3\t[Weight=2];", output.get(4));
        assertEquals("\t4\t\t[Weight=5,Start=7,Processor=2];", output.get(5));
        assertEquals("\t2 -> 4\t[Weight=2];", output.get(6));
        assertEquals("\t3 -> 4\t[Weight=4];", output.get(7));
    }

}