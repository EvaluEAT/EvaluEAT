package com.evalueat.evalueat.web.api;

import org.springframework.stereotype.Service;
import com.evalueat.evalueat.web.api.model.InlineResponse200;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Arrays;

@Service
public class AppApiDelegateImpl implements AppApiDelegate {

   public ResponseEntity<List<InlineResponse200>> info() {
        InlineResponse200 resp = new InlineResponse200().name("Application status").value("Up and running."); 
        return ResponseEntity.ok(Arrays.asList(resp));
   }
}