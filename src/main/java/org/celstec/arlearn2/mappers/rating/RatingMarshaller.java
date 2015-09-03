package org.celstec.arlearn2.mappers.rating;

import com.google.appengine.tools.mapreduce.CorruptDataException;
import com.google.appengine.tools.mapreduce.Marshaller;
import com.google.common.base.Charsets;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

/**
 * Created by str on 18/06/14.
 */
public class RatingMarshaller extends Marshaller<Rating> {

    @Override
    public ByteBuffer toBytes(Rating object) {
        return stringToBytes(object.getAmoutOfRatings()+":"+object.getTotalRating());
    }

    private ByteBuffer stringToBytes(String object) {
        return Charsets.UTF_8.encode(object);

    }

    @Override
    public Rating fromBytes(ByteBuffer b) {
        String s = stringFromBytes(b);
        String aorString = s.substring(0,s.indexOf(":"));
        String trString= s.substring(s.indexOf(":")+1);

        long amountOfRatings = Long.parseLong(aorString);
        long totalRating = Long.parseLong(trString);
        return new Rating(amountOfRatings,totalRating);
    }

    private String stringFromBytes(ByteBuffer b) {
        try {
            return Charsets.UTF_8.newDecoder().decode(b).toString();
        } catch (CharacterCodingException e) {
            throw new CorruptDataException("Could not decode string ", e);
        }
    }

}
