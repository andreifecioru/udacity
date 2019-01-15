package com.example.android.funtravel.common.fixtures;

import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.common.model.OfferType;
import com.example.android.funtravel.common.model.Offers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.funtravel.common.fixtures.Utils.*;


public class OfferFixtures {
    private static final List<Offer> mCityscapeOffers = new ArrayList<>();
    private static final List<Offer> mTrekkingOffers = new ArrayList<>();
    private static final List<Offer> mResortOffers = new ArrayList<>();

    static {
        mTrekkingOffers.add(Offers.create(
            "Explore the mountain trail in Retezat",
            "The scenery is fantastic. The nature is raw. The path is untraveled. Loose yourself" +
                    "in the wild nature of Romania's Carpatian mountains!",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-001.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Private or join a group tours in Peru",
            "Adventures, culture, hiking, trekking and expeditions: " +
                    "If you are going to book our individual/private tours for your holidays, " +
                    "and when you are sure that you are going to do some other options like trekking, " +
                    "climbing or expedition trips within the Cordillera Blanca or Cordillera Huayhuash with us. " +
                    "You can choose to do a tour alone or you can join an open group.",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-002.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Hiking, Trekking and Mountaineering in Peru",
            "Peruvian hiking, treks and mountains: The Cordillera Blanca - The Highest Tropical " +
                    "Tropica Mountain Range in the World: visit the most beautiful places in Peru, we have " +
                    "treks such as the Alpamayo trekking circuit, trekking Santa Cruz, trekking in the Cordillera " +
                    "Huayhuash, hiking cicuit Huascarán, trekking Alpamayo, climb Tocllaraju, climb Ishinca, " +
                    "climb Vallunaraju.",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-003.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Expeditions over 6000m Cordillera Blanca",
            "Peru expeditions: The region of the Sierra includes the Altiplano and the highest " +
                    "elevations of the Andes. In the Cordillera Blanca rises the highest peak in Peru and one " +
                    "of the highest in the Andes, the Huascarán (6,748 m altitude), climb the most famous mountains " +
                    "of Peru; suchs Nevado Pisco, Maparaju, Andavirte, ",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-004.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Peru: Hiking, Trekking Santa Cruz ",
            "Trekking Santa Cruz: A very popular and short trek in to the Cordillera Blanca " +
                    "with spectacular views of the surrounding mountains, lagoons and a demanding ascend to " +
                    "the pass \"Punta Union\" at an altitude of 4759 m.",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-005.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Spring Trekking in Nepal and Bhutan",
            "For those after warmer daytime temperatures, blossoming flora and generally stable " +
                    "weather, spring is the ideal season for trekking in Nepal and Bhutan. With so many " +
                    "guaranteed trips on offer this March and April, you have lots of opportunities to " +
                    "experience spring in the Himalayas of Nepal and Bhutan.",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-006.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Taste of Tasmania",
            "Start and end in Hobart! With the hiking & trekking tour Taste of Tasmania (6 Days Basix), " +
                    "you have a 6 day tour package taking you through Hobart, Australia and 25 other destinations in " +
                    "Australia. Taste of Tasmania (6 Days Basix) is a small group tour that includes accommodation in a " +
                    "hostel as well as an expert guide, meals, transport and more.",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-007.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "",
            "",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-008.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Walking the Amalfi Coast",
            "Start and end in Naples! With the hiking & trekking tour Walking the Amalfi Coast, " +
                    "you have a 8 day tour package taking you through Naples, Italy and 7 other destinations in Italy." +
                    " Walking the Amalfi Coast includes accommodation in a hotel as well as an expert guide, meals, " +
                    "transport.",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-009.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));

        mTrekkingOffers.add(Offers.create(
            "Inca Trail Express",
            "Start and end in Cusco! With the hiking & trekking tour Inca Trail Express, you have a 7 " +
                    "day tour package taking you through Cusco, Peru and 4 other destinations in Peru. Inca Trail " +
                    "Express includes accommodation in a hotel as well as an expert guide, meals and more.",
            OfferType.TREKKING,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-trekking-010.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-trekking-001.mp4"
        ));
    }

    static {
        mCityscapeOffers.add(Offers.create(
            "Visit Bucharest in one short weekend!",
            "Bucharest is a fascinating place to visit. " +
                    "The 'Old Town' are has all the bars and clubs you would need for an " +
                    "amazing night our with your friends. The Opera House satisfies all your" +
                    "classical music cravings. Speaking of cravings, there is also a long " +
                    "list of restaurants for all you foodies out there!",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-001.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "Paris City Tour - Paris visit by double-decker bus",
            "From our agency in the centre of Paris, you could discover Paris during " +
                    "two hours within a double-decker bus totally revisited with translucent roof, " +
                    "retractable windows ... with individual audio guides (9 languages : French, English, " +
                    "Spanish, Italian, Russian, Japanese, Portuguese, German) ... and second floor heating. " +
                    "Two hours of discover even if it is raining!",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-002.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "6 views to visit London for",
            "Sure, everyone who comes to London’s aware of the fact you can catch the " +
                    "capital’s sights from on high thanks to the London Eye on the South Bank, " +
                    "but let’s be honest, *everyone* goes there and does that. What about the less " +
                    "clichéd vantage points for some truly distinguished, unforgettable cityscape panoramas?",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-003.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "Barcelona Contemporary Cityscape",
            "Sail past long lines of tourists trying to gain entry to Gaudi’s La Sagrada Familia " +
                    "with this 1.5-hour priority access tour of the modernist masterpiece. Accompanied by a guide, " +
                    "head straight inside to get fascinating insight into the history of the UNESCO World Heritage-listed " +
                    "cathedral.",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-004.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "Madrid museums tour",
            "1. Toledo Full Day Private Tour from Madrid. \n" +
                    "2. Segovia and Toledo One Day Tour with Segovia Fortress and Toledo Cathedral Entry. \n" +
                    "3. Prado Museum and Reina Sofia Museum Guided Tour with Skip the Line Tickets.",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-005.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "Looking for things to do in Berlin?",
            "Munch on delicious cake and sip a steaming-hot coffee as you cruise through the open water. " +
                    "See incredible landmarks, take pictures, and relax for an hour. Enjoy a piping hot, gourmet pizza on a " +
                    "leisurely cruise down the river Spree. You’ll pass central Berlin and the most famous monuments in the city.",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-006.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "Dublin castle tour",
            "Standing at a strategic sit at the junction of the River Liffey and the Poddle, " +
                    "Dublin Castle is the heart of the historical city. Constructed in the early thirteenth " +
                    "century on the site of a Viking settlement, Dublin Castle served for centuries as the " +
                    "headquarters of English, and later British, administration in Ireland.In 1922, " +
                    "following Ireland's independence, Dublin Castle was handed over to the new Irish " +
                    "government. It is now a major government complex and a key tourist attraction.",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-007.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "Copenhagen Panoramic City Tour",
            "Excellent 1 hour tour showing you Little Mermaid, the city skyline (Palace Dome and " +
                    "Opera House) as well as canals tourCopenhagen Panoramic City Tour. When you visit Copenhagen, " +
                    "this is a must activity to do to have a panoramic city tour, absolutely worth it.",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-008.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "",
            "",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-009.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));

        mCityscapeOffers.add(Offers.create(
            "Meaning. Color. Life",
            "Whether you want to follow the footsteps of your favorite artist, explore Vienna’s " +
                    "rise through different architectural époques or get drawn into its contemporary youth - and subcultures, " +
                    "I’ll make sure you have an enjoyable and insightful journey.",
            OfferType.CITYSCAPE,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-cityscape-010.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-cityscape-001.mp4"
        ));
    }

    static {
        mResortOffers.add(Offers.create(
            "Hotel Theoxenia, Greece",
            "Theoxenia hotel is a 4 star hotel located 30 metres from the beach " +
                    "in Ouranoupolis 130 km from Thessaloniki.\n" +
                    "Theoxenia hotel complex is situated at two kilometres from Ouranoupoli," +
                    " Halkidiki, the peninsula of Mount Athos, the holy mountain. It combines " +
                    "the blue sea overlooking the island of Ammouliani and green forests surrounding it.",
            OfferType.RESORT,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-001.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
            "Hotel Dolphin Beach, Greece",
            "The famous Kriopigi Beach Hotel is situated in a holiday resort near the " +
                    "village of Kriopigi. Built in the most privileged location of the village, " +
                    "at 120 m above sea level, through the Woods, offering unlimited views of " +
                    "the Gulf of Toronean and the whole area of Sithonia.",
            OfferType.RESORT,
            getRandomPrice(),
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-002.jpg",
            "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Hotel Dolphin Beach, Greece",
                "The famous Kriopigi Beach Hotel is situated in a holiday resort near the " +
                        "village of Kriopigi. Built in the most privileged location of the village, " +
                        "at 120 m above sea level, through the Woods, offering unlimited views of " +
                        "the Gulf of Toronean and the whole area of Sithonia.",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-003.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Hotel Elena, Bulgaria",
                "Hotel Elena is located in the southern park side of Golden Sands resort, " +
                        "approximately 200 m from the beach and the commercial area of the resort (about 150m). " +
                        "The hotel was built in 2004. Located less than 5 minutes from the main street and the most " +
                        "popular attractions and nightclubs, at the same time it is located in the quiet and peaceful " +
                        "part of the resort.",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-004.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Hotel Berlin Golden Beach, Bulgaria",
                "Hotel Pliska has a main restaurant, a cafe, a pool bar. The hotel offers additional " +
                        "services such as currency exchange, ironing, personal safe, room service. There are children " +
                        "playground, conference hall with a capacity of 60 seats.",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-005.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Hotel Paradise Green Park, Bulgaria",
                "Park Hotel Golden Beach has a main restaurant with summer terrace, lobby bar, pool bar, " +
                        "barbeque. The hotel has also a conference hall with 60 seats, games room - billiards, mini football. " +
                        "Playground, kids club for children from 4 to 11,99 years, animation program and evening shows.",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-006.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Hotel Topola Skies Aquapark, Bulgaria",
                "This hotel consists of 4 buildings and offers accommodation in modernly furnished " +
                        "rooms and suites with sea, pool and golf views. The rooms have air conditioning, table " +
                        "and chairs, cable TV, electric kettle, refrigerator, balcony with table and chairs. " +
                        "One-bedroom apartments consist of one bedroom, a living room with a sofa bed, a " +
                        "kitchenette with a small fridge and kettle for heating water",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-007.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Hotel Planeta And Aquapark, Bulgaria",
                "Hotel Planeta & Spa is located in the center of Sunny Beach resort. " +
                        "The distance to the beach is less than 70 meters. Planeta is a luxury " +
                        "furnished hotel, it offers congress halls, SPA center, fitness room and many " +
                        "entertainment possibilities.",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-008.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Complex Hotelier Club Dunarea, Romania",
                "Situat la ieşirea din staţiunea Eforie Nord, în spatele gării, " +
                        "la  peste 380-400 m de mare, Complexul hotelier se întinde pe o suprafaţă de 20.000 mp, " +
                        "inconjurat de pomi si verdeata, fiind o oaza de liniste si confort de care turistii au " +
                        "nevoie pentru a se relaxa in concediu. ",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-009.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));

        mResortOffers.add(Offers.create(
                "Hotel Gala, Spain",
                "Hotel Gala is located 15 km from Southern Tenerife airport in the tourist center " +
                        "of Playa de las Americas, on the second line of the beach (200 m from the beach).\n" +
                        "It was built in 1989 and last renovated in 2006.",
                OfferType.RESORT,
                getRandomPrice(),
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-resort-010.jpg",
                "http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-resort-001.mp4"
        ));
    }

    public static List<Offer> getOffers(OfferType type, int count) {
        List<Offer> source;
        switch (type) {
            case CITYSCAPE:
                source = mCityscapeOffers;
                break;
            case TREKKING:
                source = mTrekkingOffers;
                break;
            case RESORT:
                source = mResortOffers;
                break;
            default:
                throw new IllegalArgumentException("Unknown offer type: " + type.asString());
        }

        List<Offer> result = new ArrayList<>();
        for (int i = 0; i< count; i++) {
            if (!source.isEmpty()) {
                result.add(randomChoiceOf(source));
            }
        }

        return Collections.unmodifiableList(result);
    }

    private OfferFixtures() {
        throw new IllegalStateException("Utility class");
    }
}
