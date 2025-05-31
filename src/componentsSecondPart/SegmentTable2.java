package componentsSecondPart;

import components.SegmentTable;

public class SegmentTable2 extends SegmentTable{

    public SegmentTable2(){
        super();
    }

    public void setSegmentTable(byte[] segments_size,int param_size) {

        if(segments_size.length == 2){
            super.setSegmentTable(segments_size, 16384);   
        }else{
            this.segmentTable[0] = param_size;
            
            int CS = ((int)(segments_size[0] << 8)|(int)(segments_size[1]) & 0xFF);
            int DS = ((int)(segments_size[2] << 8)|(int)(segments_size[3]) & 0xFF);
            int ES = ((int)(segments_size[4] << 8)|(int)(segments_size[5]) & 0xFF);
            int SS = ((int)(segments_size[6] << 8)|(int)(segments_size[7]) & 0xFF);
            int KS = ((int)(segments_size[8] << 8)|(int)(segments_size[9]) & 0xFF);
            int i = 1;
            
            if(KS > 0 ){
                    this.segmentTable[i] = (param_size << 16) | (KS & 0xFFFF);
                i++;
            }
            //CS
            if(i == 1){
                this.segmentTable[i] = (param_size << 16) | (CS & 0xFFFF);
            }else{
                this.segmentTable[i] = ((getBase(i-1) + getSize(i-1)) << 16) | (CS & 0xFFFF);
            }
            this.CS = i;
            i++;

            if(DS > 0 ){
                this.segmentTable[i] = ((getBase(i-1) + getSize(i-1))  << 16) | (DS & 0xFFFF);
                i++;
            }
            if(ES > 0 ){
                this.segmentTable[i] = ((getBase(i-1) + getSize(i-1))  << 16) | (ES & 0xFFFF);
                i++;
            }
            if(SS > 0 ){
                this.segmentTable[i] = ((getBase(i-1) + getSize(i-1))  << 16) | (SS & 0xFFFF);

            }
        }


    }

    public void loadSegmentTable(byte[] segment_table){
        int k = 0;
        int segment = 0;
        for(int i = 0; i < segment_table.length; i+=4){
            segment = 0;
            for(int j = 0; j < 4; j++){
                segment = (segment << 8) | (int)(segment_table[i+j] & 0xFF);
            }
            this.segmentTable[k] = segment;
            k++;
        }
    }
    
}
