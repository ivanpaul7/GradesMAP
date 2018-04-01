package lab.repository.statistics;



public class StatisticsRepository {

//    public void saveToPDF(String numeFisier, List<StudentHOF> list) throws IOException {
//
////        //Creating PDF document object
////        PDDocument document = new PDDocument();
////
////        //Saving the document
////        document.save("src\\lab\\docs\\statisticsPDF\\"+numeFisier);
////
////        System.out.println("PDF created");
////
////        //Closing the document
////        document.close();
//
//
//
//
//
////        File file = new File("fileName.pdf");
////        PDDocument document = PDDocument.load(file);
////        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
////        stripper.setSortByPosition( true );
////        Rectangle rect1 = new Rectangle( 50, 140, 60, 20 );
////        Rectangle rect2 = new Rectangle( 110, 140, 20, 20 );
////        stripper.addRegion( "row1column1", rect1 );
////        stripper.addRegion( "row1column2", rect2 );
////        List allPages = document.getDocumentCatalog().getOutputIntents();
////        PDPage firstPage = (PDPage)allPages.get( 2 );
////        stripper.extractRegions( firstPage );
////        System.out.println(stripper.getTextForRegion( "row1column1" ));
////        System.out.println(stripper.getTextForRegion( "row1column2" ));
//
//
//
//        TableBuilder tableBuilder = new TableBuilder()
//                .addColumnOfWidth(300)
//                .addColumnOfWidth(120)
//                .addColumnOfWidth(70)
//                .setFontSize(8)
//                .setFont(PDType1Font.HELVETICA);
//
//// Header ...
//        tableBuilder.addRow(new RowBuilder()
//                .add(Cell.withText("This is right aligned without a border").setHorizontalAlignment(RIGHT))
//                .add(Cell.withText("And this is another cell"))
//                .add(Cell.withText("Sum").setBackgroundColor(Color.ORANGE))
//                .setBackgroundColor(Color.BLUE)
//                .build());
//
//// ... and some cells
//        for (int i = 0; i < 10; i++) {
//            tableBuilder.addRow(new RowBuilder()
//                    .add(Cell.withText(i).withAllBorders())
//                    .add(Cell.withText(i * i).withAllBorders())
//                    .add(Cell.withText(i + (i * i)).withAllBorders())
//                    .setBackgroundColor(i % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE)
//                    .build());
//        }
//
//        final PDDocument document = new PDDocument();
//        final PDPage page = new PDPage(PDRectangle.A4);
//        document.addPage(page);
//
//        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
//
//// Define the starting point
//        final float startY = page.getMediaBox().getHeight() - 50;
//        final int startX = 50;
//
//// Draw!
//        (new TableDrawer(contentStream, tableBuilder.build(), startX, startY)).draw();
//        contentStream.close();
//
//        document.save("target/sampleWithColorsAndBorders.pdf");
//        document.close();
//
//    }
}