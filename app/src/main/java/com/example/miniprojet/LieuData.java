package com.example.miniprojet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LieuData {

    private static Map<String, String> step(String heure, String activite) {
        Map<String, String> m = new HashMap<>();
        m.put("heure",    heure);
        m.put("activite", activite);
        return m;
    }

    @SafeVarargs
    private static List<Map<String, String>> prog(Map<String, String>... steps) {
        List<Map<String, String>> list = new ArrayList<>();
        for (Map<String, String> s : steps) list.add(s);
        return list;
    }

    public static List<Lieu> getAll() {
        List<Lieu> list = new ArrayList<>();

        list.add(new Lieu("1","Sidi Bou Said","Tunis",
                "Magical blue and white village overlooking the Mediterranean sea.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Sidi_Bou_Said_-_Tunis.jpg/800px-Sidi_Bou_Said_-_Tunis.jpg",
                "Village",
                prog(step("09:00","Arriver au village, se promener dans les ruelles"),
                        step("10:30","Café Sidi Chebaane avec vue mer"),
                        step("12:00","Déjeuner restaurant El Marsaa"),
                        step("14:00","Visite musée Dar El Annabi"),
                        step("16:00","Shopping souvenirs et artisanat"),
                        step("18:00","Coucher de soleil au bord de la falaise"))));

        list.add(new Lieu("2","Carthage","Tunis",
                "Ancient ruins of one of the greatest civilizations in history.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Carthage_Byrsa.jpg/800px-Carthage_Byrsa.jpg",
                "Histoire",
                prog(step("09:00","Musée National de Carthage"),
                        step("11:00","Ruines des Thermes d'Antonin"),
                        step("12:30","Déjeuner à Carthage Hannibal"),
                        step("14:00","Tophet et sanctuaire punique"),
                        step("16:00","Port punique et musée"),
                        step("17:30","Vue panoramique colline Byrsa"))));

        list.add(new Lieu("3","Médina de Tunis","Tunis",
                "UNESCO World Heritage Site — one of the Arab world's greatest medinas.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Tunis_medina.jpg/800px-Tunis_medina.jpg",
                "Culture",
                prog(step("09:00","Mosquée Zitouna — plus grande mosquée de Tunis"),
                        step("10:30","Souk des parfums et épices"),
                        step("12:00","Déjeuner Dar El Jeld"),
                        step("14:00","Musée du Bardo à proximité"),
                        step("16:00","Souk des chéchias et artisanat"),
                        step("18:00","Café M'rabet — café traditionnel"))));

        list.add(new Lieu("4","Bardo Museum","Tunis",
                "Home to one of the world's finest collections of Roman mosaics.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Bardo_National_Museum_2.jpg/800px-Bardo_National_Museum_2.jpg",
                "Musée",
                prog(step("09:30","Aile punique et berbère"),
                        step("11:00","Grande salle des mosaïques romaines"),
                        step("12:30","Cafétéria du musée"),
                        step("14:00","Collection islamique"),
                        step("15:30","Aile paléochrétienne"),
                        step("16:30","Boutique souvenirs"))));

        list.add(new Lieu("5","La Marsa","Tunis",
                "Chic coastal suburb with beautiful beaches and vibrant café scene.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/La_Marsa_beach.jpg/800px-La_Marsa_beach.jpg",
                "Plage",
                prog(step("09:00","Plage de La Marsa Corniche"),
                        step("11:00","Café Saf Saf — café emblématique"),
                        step("13:00","Déjeuner fruits de mer"),
                        step("15:00","Promenade corniche"),
                        step("17:00","Shopping galerie La Marsa"),
                        step("19:00","Sunset dîner en bord de mer"))));

        list.add(new Lieu("6","Gammarth","Tunis",
                "Upscale beach resort area with luxury hotels and pristine white sand.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Gammarth_beach_Tunisia.jpg/800px-Gammarth_beach_Tunisia.jpg",
                "Plage",
                prog(step("10:00","Plage Les Ombrelles"),
                        step("12:00","Déjeuner La Goulette"),
                        step("14:00","Sports nautiques jet-ski"),
                        step("16:00","Spa et hammam hôtel"),
                        step("19:00","Rooftop bar sunset"))));

        list.add(new Lieu("7","Djerba","Médenine",
                "Enchanting island of sun, beaches and authentic Tunisian culture.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Djerba_Houmt_Souk.jpg/800px-Djerba_Houmt_Souk.jpg",
                "Île",
                prog(step("09:00","Houmt Souk — marché traditionnel"),
                        step("11:00","Synagogue La Ghriba"),
                        step("13:00","Déjeuner restaurant Haroun"),
                        step("15:00","Plage Sidi Mahrez"),
                        step("17:00","Village potiers Guellala"),
                        step("19:00","Coucher soleil Fort Borj Ghazi Mustapha"))));

        list.add(new Lieu("8","Tozeur","Tozeur",
                "Gateway to the Sahara — oases, salt lakes and Star Wars filming locations.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Tozeur_oasis.jpg/800px-Tozeur_oasis.jpg",
                "Désert",
                prog(step("07:00","Lever du soleil sur les dunes"),
                        step("09:00","Oasis palmiers Chott el-Jérid"),
                        step("11:00","Village de sel Chott el-Jérid"),
                        step("13:00","Déjeuner restaurant Dar Horchani"),
                        step("15:00","Ancienne médina Ouled El Hadef"),
                        step("17:00","Balade dromadaire dunes"),
                        step("20:00","Dîner sous les étoiles camp bédouin"))));

        list.add(new Lieu("9","Douz","Kébili",
                "The Gateway to the Sahara — famous for its annual festival and camel trekking.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Douz_Tunisia.jpg/800px-Douz_Tunisia.jpg",
                "Désert",
                prog(step("07:00","Trek chameau lever soleil"),
                        step("10:00","Marché hebdomadaire de Douz"),
                        step("12:30","Déjeuner couscous berbère"),
                        step("14:30","Musée du Sahara"),
                        step("16:30","4x4 dans les dunes Grand Erg"),
                        step("20:00","Nuit camp sous les étoiles"))));

        list.add(new Lieu("10","Matmata","Gabès",
                "Troglodyte village — home of Luke Skywalker's house in Star Wars.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Matmata_Tunisia.jpg/800px-Matmata_Tunisia.jpg",
                "Désert",
                prog(step("09:00","Maisons troglodytes traditionnelles"),
                        step("10:30","Hôtel Sidi Driss — décor Star Wars"),
                        step("12:00","Déjeuner couscous dans grotte"),
                        step("14:00","Village berbère Tijma"),
                        step("16:00","Ksar Hadada fortifié"),
                        step("18:00","Coucher soleil panorama désert"))));

        list.add(new Lieu("11","Hammamet","Nabeul",
                "Tunisia's premier beach resort on the beautiful Mediterranean coast.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/Hammamet_medina.jpg/800px-Hammamet_medina.jpg",
                "Plage",
                prog(step("09:00","Médina de Hammamet"),
                        step("10:30","Forteresse Kasbah vue mer"),
                        step("12:00","Déjeuner restaurant La Bella Vista"),
                        step("14:00","Plage Hammamet Nord"),
                        step("16:00","Yasmine Hammamet port de plaisance"),
                        step("18:00","Sunset beach bar"))));

        list.add(new Lieu("12","Nabeul","Nabeul",
                "Capital of Tunisian pottery and ceramics — colorful craft capital.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a8/Nabeul_ceramics.jpg/800px-Nabeul_ceramics.jpg",
                "Culture",
                prog(step("09:00","Marché du vendredi — le plus grand de Tunisie"),
                        step("11:00","Ateliers potiers et céramistes"),
                        step("13:00","Déjeuner restaurant Lella"),
                        step("14:30","Musée archéologique de Nabeul"),
                        step("16:00","Shopping artisanat — poterie, broderie"),
                        step("18:00","Plage Nabeul sunset"))));

        list.add(new Lieu("13","Kairouan","Kairouan",
                "One of Islam's holiest cities — UNESCO World Heritage Site.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Kairouan_Grande_Mosquee.jpg/800px-Kairouan_Grande_Mosquee.jpg",
                "Religion",
                prog(step("09:00","Grande Mosquée — 4ème lieu saint de l'Islam"),
                        step("10:30","Médina UNESCO — déambulation"),
                        step("12:00","Déjeuner restaurant Sabra"),
                        step("14:00","Piscines des Aghlabides"),
                        step("15:30","Mosquée des Trois Portes"),
                        step("16:30","Souk des tapis — tapis de Kairouan célèbres"),
                        step("18:00","Coucher soleil remparts"))));

        list.add(new Lieu("14","El Djem","Mahdia",
                "Home to one of the best preserved Roman amphitheaters in the world.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/El_Djem_Amphitheatre_2.jpg/800px-El_Djem_Amphitheatre_2.jpg",
                "Histoire",
                prog(step("09:00","Amphithéâtre romain — 3ème plus grand du monde"),
                        step("11:00","Sous-sols et galeries secrètes"),
                        step("12:30","Déjeuner restaurant El Firdaous"),
                        step("14:00","Musée archéologique El Djem"),
                        step("15:30","Village traditionnel El Djem"),
                        step("17:00","Retour avec vue dorée amphithéâtre"))));

        list.add(new Lieu("15","Mahdia","Mahdia",
                "Historic Fatimid capital on a beautiful peninsula with pristine beaches.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Mahdia_Tunisia.jpg/800px-Mahdia_Tunisia.jpg",
                "Plage",
                prog(step("09:00","Skifa El Kahla — porte fatimide"),
                        step("10:30","Médina de Mahdia"),
                        step("12:00","Déjeuner fruits de mer port"),
                        step("14:00","Plage El Borj"),
                        step("16:00","Cap Afrique phare panoramique"),
                        step("18:00","Coucher soleil pointe péninsule"))));

        list.add(new Lieu("16","Sfax","Sfax",
                "Tunisia's second city — historic medina and gateway to the Kerkennah Islands.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Sfax_Medina.jpg/800px-Sfax_Medina.jpg",
                "Culture",
                prog(step("09:00","Médina de Sfax — UNESCO"),
                        step("10:30","Musée régional des arts et traditions"),
                        step("12:00","Déjeuner restaurant Chez Nous"),
                        step("14:00","Souk des forgerons et artisans"),
                        step("16:00","Port de Sfax et corniche"),
                        step("18:00","Îles Kerkennah en ferry"))));

        list.add(new Lieu("17","Kerkennah","Sfax",
                "Peaceful archipelago of traditional fishing villages and transparent waters.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Kerkennah_Island.jpg/800px-Kerkennah_Island.jpg",
                "Île",
                prog(step("09:00","Ferry Sfax-Kerkennah"),
                        step("10:00","Village Ouled Yaneg"),
                        step("12:00","Déjeuner poulpe et poisson frais"),
                        step("14:00","Plage transparente El Attaya"),
                        step("16:00","Pêche traditionnelle charfia"),
                        step("18:00","Retour ferry sunset"))));

        list.add(new Lieu("18","Sousse","Sousse",
                "Pearl of the Sahel — stunning medina, beaches and vibrant nightlife.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Sousse_medina.jpg/800px-Sousse_medina.jpg",
                "Culture",
                prog(step("09:00","Médina UNESCO de Sousse"),
                        step("10:30","Grande Mosquée et Ribat"),
                        step("12:00","Déjeuner restaurant Etoile"),
                        step("14:00","Musée archéologique mosaïques"),
                        step("15:30","Plage Boujaffar"),
                        step("17:00","Port el Kantaoui marina"),
                        step("19:00","Sunset dîner port"))));

        list.add(new Lieu("19","Monastir","Monastir",
                "Birthplace of Habib Bourguiba — beautiful ribat and marina.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Ribat_of_Monastir.jpg/800px-Ribat_of_Monastir.jpg",
                "Histoire",
                prog(step("09:00","Ribat de Monastir — forteresse islamique"),
                        step("10:30","Mausolée Bourguiba"),
                        step("12:00","Déjeuner port de plaisance"),
                        step("14:00","Médina et grande mosquée"),
                        step("15:30","Plage Monastir"),
                        step("17:00","Corniche et marina sunset"))));

        list.add(new Lieu("20","Tabarka","Jendouba",
                "Coral coast gem — famous for diving, jazz festival and ancient Numidian ruins.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Tabarka_Tunisia.jpg/800px-Tabarka_Tunisia.jpg",
                "Plage",
                prog(step("09:00","Plongée récif corail — eaux cristallines"),
                        step("11:00","Forteresse génoise panoramique"),
                        step("13:00","Déjeuner homard et langouste"),
                        step("15:00","Les Aiguilles — formations rocheuses"),
                        step("17:00","Plage Tabarka"),
                        step("19:00","Jazz café en bord de mer"))));

        list.add(new Lieu("21","Ain Draham","Jendouba",
                "Mountain village in the Kroumirie — cork forests and cool climate.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Ain_Draham_Tunisia.jpg/800px-Ain_Draham_Tunisia.jpg",
                "Nature",
                prog(step("09:00","Forêt de chênes-lièges Kroumirie"),
                        step("11:00","Village montagnard traditionnel"),
                        step("13:00","Déjeuner grillades et fromages locaux"),
                        step("15:00","Randonnée Djebel Bir"),
                        step("17:00","Sources naturelles d'eau douce"),
                        step("19:00","Dîner cheminée — spécialités montagnardes"))));

        list.add(new Lieu("22","Bulla Regia","Jendouba",
                "Unique Roman city with stunning underground villas.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Bulla_Regia.jpg/800px-Bulla_Regia.jpg",
                "Histoire",
                prog(step("09:00","Villas souterraines romaines"),
                        step("10:30","Mosaïques in situ exceptionnelles"),
                        step("12:00","Pique-nique site archéologique"),
                        step("14:00","Forum et thermes"),
                        step("15:30","Temple d'Apollon"),
                        step("17:00","Musée de site"))));

        list.add(new Lieu("23","Dougga","Béja",
                "Best preserved Roman town in North Africa — UNESCO World Heritage.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Dougga_theatre.jpg/800px-Dougga_theatre.jpg",
                "Histoire",
                prog(step("09:00","Capitole romain — symbole de Dougga"),
                        step("10:30","Théâtre romain 3500 places"),
                        step("12:00","Thermes des Cyclopes"),
                        step("13:00","Pique-nique panoramique"),
                        step("14:30","Temple de Saturne"),
                        step("16:00","Nécropole libyco-punique"),
                        step("17:30","Vue coucher soleil vallée"))));

        list.add(new Lieu("24","Zaghouan","Zaghouan",
                "Mountain town with Roman temple and aqueduct supplying ancient Carthage.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Zaghouan_temple.jpg/800px-Zaghouan_temple.jpg",
                "Nature",
                prog(step("09:00","Temple des eaux romain"),
                        step("10:30","Source naturelle Zaghouan"),
                        step("12:00","Déjeuner spécialités locales"),
                        step("14:00","Parc national Djebel Zaghouan"),
                        step("16:00","Randonnée sommet — vue 360°"),
                        step("18:00","Retour ville coucher soleil"))));

        list.add(new Lieu("25","Mides","Tozeur",
                "Spectacular canyon village on the Algerian border.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Mides_Canyon_Tunisia.jpg/800px-Mides_Canyon_Tunisia.jpg",
                "Désert",
                prog(step("08:00","Canyon de Mides au lever du soleil"),
                        step("10:00","Village berbère abandonné"),
                        step("12:00","Déjeuner oasis de Chebika"),
                        step("14:00","Oasis Tamerza — cascade"),
                        step("16:00","Gorges de Selja — canyon rouge"),
                        step("18:00","Coucher soleil désert"))));

        list.add(new Lieu("26","Tamerza","Tozeur",
                "Oasis village with stunning waterfalls surrounded by desert mountains.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tamerza_oasis_Tunisia.jpg/800px-Tamerza_oasis_Tunisia.jpg",
                "Nature",
                prog(step("09:00","Cascade Tamerza — oasis montagnarde"),
                        step("10:30","Ancien village abandonné"),
                        step("12:00","Déjeuner Hôtel Tamerza Palace"),
                        step("14:00","Piscine naturelle oasis"),
                        step("16:00","Gorges rouges balade"),
                        step("18:00","Stars and dunes soirée"))));

        list.add(new Lieu("27","Chott El Jérid","Tozeur",
                "Largest salt lake in North Africa — otherworldly lunar landscape.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Chott_El_Jerid.jpg/800px-Chott_El_Jerid.jpg",
                "Désert",
                prog(step("07:00","Lever soleil sur le lac de sel"),
                        step("09:00","Traversée chaussée Tozeur-Kébili"),
                        step("11:00","Mirages et cristaux de sel"),
                        step("13:00","Déjeuner El Hamma"),
                        step("15:00","Oasis Kébili"),
                        step("17:00","Coucher soleil reflets dorés"))));

        list.add(new Lieu("28","Ksar Ouled Soltane","Tataouine",
                "Most beautiful ksar in Tunisia — perfectly preserved Berber granary.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Ksar_Ouled_Soltane.jpg/800px-Ksar_Ouled_Soltane.jpg",
                "Désert",
                prog(step("09:00","Ksar Ouled Soltane — ghorfas 4 étages"),
                        step("11:00","Village berbère traditionnel"),
                        step("12:30","Déjeuner sous les ghorfas"),
                        step("14:00","Ksar Hadada — hôtel Star Wars"),
                        step("16:00","Chenini — village perché"),
                        step("18:00","Coucher soleil désert rouge"))));

        list.add(new Lieu("29","Chenini","Tataouine",
                "Dramatic troglodyte village carved into a hillside in the deep south.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Chenini_village_Tunisia.jpg/800px-Chenini_village_Tunisia.jpg",
                "Désert",
                prog(step("09:00","Ascension village perché"),
                        step("10:30","Mosquée blanche — vue panoramique"),
                        step("12:00","Déjeuner maison berbère"),
                        step("14:00","Grottes habitations troglodytes"),
                        step("16:00","Cimetière sept dormants"),
                        step("18:00","Coucher soleil sommet"))));

        list.add(new Lieu("30","Tataouine","Tataouine",
                "Gateway to the ksour region — ancient Berber fortified granaries.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Tataouine_ksar.jpg/800px-Tataouine_ksar.jpg",
                "Désert",
                prog(step("09:00","Ksar de Tataouine"),
                        step("10:30","Marché berbère hebdomadaire"),
                        step("12:00","Déjeuner spécialités du sud"),
                        step("14:00","Ksar Mégabla"),
                        step("16:00","Ghomrassen village"),
                        step("18:00","Ciel étoilé Tataouine"))));

        list.add(new Lieu("31","Bizerte","Bizerte",
                "Northernmost city in Africa — beautiful old port and stunning beaches.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Bizerte_old_port.jpg/800px-Bizerte_old_port.jpg",
                "Plage",
                prog(step("09:00","Vieux port de Bizerte"),
                        step("10:30","Kasbah et Fort d'Espagne"),
                        step("12:00","Déjeuner fruits de mer port"),
                        step("14:00","Plage Remel — la plus belle du nord"),
                        step("16:00","Lac de Bizerte flamants roses"),
                        step("18:00","Coucher soleil vieux port"))));

        list.add(new Lieu("32","Cap Bon","Nabeul",
                "Scenic peninsula with vineyards, citrus groves and rocky coastline.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Cap_Bon_Tunisia.jpg/800px-Cap_Bon_Tunisia.jpg",
                "Nature",
                prog(step("09:00","Pointe Cap Bon — phare"),
                        step("10:30","Vignobles et citronniers"),
                        step("12:00","Déjeuner Kélibia port"),
                        step("14:00","Fort byzantin Kélibia"),
                        step("15:30","Plage Mansourah"),
                        step("17:30","Coucher soleil cap"))));

        list.add(new Lieu("33","Kélibia","Nabeul",
                "Fishing village with a stunning fortress above turquoise waters.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Kelibia_fort.jpg/800px-Kelibia_fort.jpg",
                "Histoire",
                prog(step("09:00","Fort byzantin — vue 360°"),
                        step("10:30","Port de pêche traditionnel"),
                        step("12:00","Déjeuner poisson grillé"),
                        step("14:00","Plage El Mansoura"),
                        step("16:00","Marché artisanal"),
                        step("18:00","Coucher soleil fort"))));

        list.add(new Lieu("34","Zarzis","Médenine",
                "Southern coastal town with beautiful beaches and olive groves.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Zarzis_beach.jpg/800px-Zarzis_beach.jpg",
                "Plage",
                prog(step("09:00","Plage Zarzis — sable blanc fin"),
                        step("11:00","Oliveraies traditionnelles"),
                        step("13:00","Déjeuner spécialités médenine"),
                        step("15:00","Spa thalasso — boue marine"),
                        step("17:00","Coucher soleil plage"),
                        step("19:00","Dîner poisson barbecue"))));

        list.add(new Lieu("35","Houmt Souk","Médenine",
                "Colorful main town of Djerba island with vibrant souks.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Houmt_Souk_Djerba.jpg/800px-Houmt_Souk_Djerba.jpg",
                "Culture",
                prog(step("09:00","Souk des potiers Guellala"),
                        step("10:30","Marché couvert Houmt Souk"),
                        step("12:00","Déjeuner restaurant Haroun"),
                        step("14:00","Musée des Arts et Traditions"),
                        step("15:30","Fort Bordj Ghazi Mustapha"),
                        step("17:00","Coucher soleil port"))));

        list.add(new Lieu("36","Ghar El Melh","Bizerte",
                "Ancient corsair port with a peaceful lagoon and historic fortresses.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Ghar_El_Melh_Tunisia.jpg/800px-Ghar_El_Melh_Tunisia.jpg",
                "Histoire",
                prog(step("09:00","Vieux port corsaire"),
                        step("10:30","Fort Génois et Fort Rass El Djebel"),
                        step("12:00","Déjeuner lagune"),
                        step("14:00","Plage Sidi Ali El Mekki"),
                        step("16:00","Lagune flamants roses"),
                        step("18:00","Coucher soleil fortifications"))));

        list.add(new Lieu("37","Ichkeul","Bizerte",
                "UNESCO Biosphere Reserve — seasonal lake vital for migratory birds.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Ichkeul_lake.jpg/800px-Ichkeul_lake.jpg",
                "Nature",
                prog(step("08:00","Observation flamants roses et oiseaux migrateurs"),
                        step("10:00","Randonnée Djebel Ichkeul"),
                        step("12:00","Pique-nique bord du lac"),
                        step("14:00","Musée du parc national"),
                        step("15:30","Buffalo d'eau du Maghreb"),
                        step("17:00","Coucher soleil lac"))));

        list.add(new Lieu("38","Beni Kheddache","Médenine",
                "Berber hilltop village with panoramic views over the south.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Beni_Kheddache.jpg/800px-Beni_Kheddache.jpg",
                "Culture",
                prog(step("09:00","Village berbère perché"),
                        step("10:30","Maison berbère traditionnelle"),
                        step("12:00","Déjeuner couscous berbère"),
                        step("14:00","Panorama désert et plaines"),
                        step("15:30","Artisanat tissage local"),
                        step("17:00","Coucher soleil panoramique"))));

        list.add(new Lieu("39","Korbous","Nabeul",
                "Thermal spa village on the Cap Bon peninsula overlooking the sea.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Korbous_Tunisia.jpg/800px-Korbous_Tunisia.jpg",
                "Nature",
                prog(step("09:00","Sources thermales naturelles — bord de mer"),
                        step("10:30","Bain thermal Aïn Atrous"),
                        step("12:00","Déjeuner restaurant terrasse mer"),
                        step("14:00","Randonnée falaises côtières"),
                        step("16:00","Bain source chaude Aïn Oktor"),
                        step("18:00","Coucher soleil golfe de Tunis"))));

        list.add(new Lieu("40","El Haouaria","Nabeul",
                "Cliffside village at the tip of Cap Bon — falconry and Roman quarries.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/El_Haouaria_Tunisia.jpg/800px-El_Haouaria_Tunisia.jpg",
                "Nature",
                prog(step("09:00","Grottes romaines Ghar El Kebir"),
                        step("10:30","Festival de fauconnerie"),
                        step("12:00","Déjeuner poulpe grillé"),
                        step("14:00","Pointe Cap Bon"),
                        step("15:30","Plage El Haouaria cristalline"),
                        step("17:30","Coucher soleil cap"))));

        list.add(new Lieu("41","Sbeitla","Kasserine",
                "Stunning Roman ruins of Sufetula — one of the best preserved forums in Africa.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Sbeitla_Roman_ruins.jpg/800px-Sbeitla_Roman_ruins.jpg",
                "Histoire",
                prog(step("09:00","Arc de triomphe d'Antonin le Pieux"),
                        step("10:00","Forum romain et trois temples"),
                        step("11:30","Basiliques chrétiennes"),
                        step("13:00","Déjeuner Sbeitla"),
                        step("14:30","Thermes romains"),
                        step("16:00","Coucher soleil sur les temples"))));

        list.add(new Lieu("42","Kasserine","Kasserine",
                "Gateway to Djebel Chambi — highest mountain in Tunisia.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/Kasserine_Tunisia.jpg/800px-Kasserine_Tunisia.jpg",
                "Nature",
                prog(step("08:00","Randonnée Djebel Chambi 1544m"),
                        step("11:00","Forêts de pins et cèdres"),
                        step("13:00","Déjeuner Kasserine"),
                        step("15:00","Parc national Chambi"),
                        step("16:30","Vue panoramique sommet"),
                        step("18:00","Coucher soleil montagnes"))));

        list.add(new Lieu("43","Gafsa","Gafsa",
                "Ancient oasis city — crossroads of the Sahara since prehistoric times.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Gafsa_oasis.jpg/800px-Gafsa_oasis.jpg",
                "Désert",
                prog(step("09:00","Piscines romaines — 2000 ans"),
                        step("10:30","Kasbah de Gafsa"),
                        step("12:00","Déjeuner spécialités locales"),
                        step("14:00","Musée régional de Gafsa"),
                        step("15:30","Oasis et palmeraies"),
                        step("17:00","Coucher soleil désert"))));

        list.add(new Lieu("44","Thuburbo Majus","Zaghouan",
                "Impressive Roman city with a well-preserved Capitol and baths.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Thuburbo_Majus.jpg/800px-Thuburbo_Majus.jpg",
                "Histoire",
                prog(step("09:00","Capitol romain"),
                        step("10:00","Thermes des Mois"),
                        step("11:00","Palestre d'été"),
                        step("12:00","Pique-nique site"),
                        step("13:30","Temple de Mercure"),
                        step("15:00","Agora et forum"),
                        step("16:00","Retour coucher soleil"))));

        list.add(new Lieu("45","Midoun","Médenine",
                "Traditional market town in the heart of Djerba island.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/26/Midoun_Djerba.jpg/800px-Midoun_Djerba.jpg",
                "Culture",
                prog(step("09:00","Marché du vendredi Midoun"),
                        step("10:30","Fantasia spectacle équestre"),
                        step("12:00","Déjeuner traditionnel"),
                        step("14:00","Village artisans potiers"),
                        step("15:30","Plage Midoun"),
                        step("17:00","Coucher soleil plage"))));

        list.add(new Lieu("46","Nabeul Ceramics","Nabeul",
                "Artisan trail through the pottery capital of Tunisia.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c6/Nabeul_pottery.jpg/800px-Nabeul_pottery.jpg",
                "Culture",
                prog(step("09:00","Atelier potier — démonstration"),
                        step("10:30","Cuisson four à bois"),
                        step("12:00","Déjeuner local"),
                        step("14:00","Marché poteries colorées"),
                        step("16:00","Atelier peinture céramique"),
                        step("17:30","Shopping souvenirs céramiques"))));

        list.add(new Lieu("47","Grombalia","Nabeul",
                "Wine capital of Tunisia — surrounded by vineyards and olive trees.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Grombalia_vineyards.jpg/800px-Grombalia_vineyards.jpg",
                "Culture",
                prog(step("09:00","Domaine viticole — dégustation"),
                        step("10:30","Vignobles à perte de vue"),
                        step("12:00","Déjeuner cave restaurant"),
                        step("14:00","Festival de la vigne"),
                        step("16:00","Oliveraies centenaires"),
                        step("18:00","Coucher soleil vignes"))));

        list.add(new Lieu("48","Béja","Béja",
                "Fertile northern city known for ancient Roman sites and wheat fields.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Beja_Tunisia.jpg/800px-Beja_Tunisia.jpg",
                "Histoire",
                prog(step("09:00","Kasbah Byzantine"),
                        step("10:30","Musée régional de Béja"),
                        step("12:00","Déjeuner couscous local"),
                        step("14:00","Campagne de blé et oliviers"),
                        step("15:30","Site Thuburbo Majus proche"),
                        step("17:00","Coucher soleil plaines"))));

        list.add(new Lieu("49","Zembra Island","Tunis",
                "Protected national park island — crystal clear waters and rare wildlife.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Zembra_island.jpg/800px-Zembra_island.jpg",
                "Île",
                prog(step("08:00","Départ bateau La Goulette"),
                        step("10:00","Plongée eaux cristallines"),
                        step("12:00","Pique-nique plage sauvage"),
                        step("14:00","Randonnée parc national"),
                        step("15:30","Observation faucons d'Éléonore"),
                        step("17:00","Retour coucher soleil en mer"))));

        list.add(new Lieu("50","Jendouba","Jendouba",
                "Gateway to northern archaeological treasures and Kroumirie forests.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Jendouba_Tunisia.jpg/800px-Jendouba_Tunisia.jpg",
                "Nature",
                prog(step("09:00","Parc national Feija"),
                        step("10:30","Forêt liège et cerfs de Barbarie"),
                        step("12:00","Déjeuner ferme locale"),
                        step("14:00","Oued Mellegue baignade"),
                        step("16:00","Vestiges romains Thuburnica"),
                        step("18:00","Retour coucher soleil"))));

        return list;
    }
}