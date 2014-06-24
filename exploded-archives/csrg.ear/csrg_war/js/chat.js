   
//<![CDATA[

	var chatBoxes = new Array();

	jQuery(document).ready(function(){
		
		jQuery('.chatList').css('display','none');
		
		jQuery(".iceInpTxt").blur(function(){
					jQuery(".iceInpTxtArea").css('border','#ccc 2px solid');
			}).focus(function(){
					
				jQuery(".iceInpTxtArea").css('border',' #f99d39 2px solid');
			});
			

	});
	
  
	
	function chatBoxMinimize()
	{
		jQuery('.chatboxcontent').css('display','none');
		jQuery('.chatboxinput').css('display','none');
	}

	function chatBoxMaximize()
	{
		jQuery('.chatboxcontent').css('display','block');
		jQuery('.chatboxinput').css('display','block');
	}

	function chatBoxClose()
	{
		jQuery('.chatbox').css('display','none');
	}

	function chatBoxOpen()
	{
		jQuery('.chatbox').css('display','block');
	}

	function chatListToggel()
	{
		var status = jQuery('.chatList').css("display");
	
		if(status == "block")
		{
			jQuery('.chatList').css('display','none');
		}
		else
		{
			jQuery('.chatList').css('display','block');
		}
	}


	function removeChatbox()
	{
		chatboxReset();
		topMenuSet();//patch for top menu
		
		//chatboxDown= chatboxDown + 1; ;
		//alert("down:"+chatboxDown);
	}


	function chatboxReset()
	{
        chatBoxes.clear(); 
        //alert(chatBoxes.length)
	} 


	function setPos(id, pos)
	{
		width = (pos)*(225+7)+ 20;
		jQuery("#cb_"+id).css('right', width+'px');
	}

	function chatBoxToggel(id)
	   {
		
			//var id = "cbc";
			var img_id = "#cbImage_"+id+" img";
	        //alert(id);
			var status = jQuery('#cbc_' + id).css("display");
			var titleLeft = jQuery(img_id).attr("width") + 10;
			var tl = titleLeft + "px";
			var titleWidth = 180 - titleLeft;
			var tw = titleWidth + "px";
			
			//alert(tw);
			
			if(status == "block")
			{
				jQuery('#cbc_' + id).css('display','none');
				jQuery('#cbi_' + id).css('display','none');
				jQuery('#cbt_' + id).css('paddingLeft','0px');
				jQuery('#cbt_' + id).css('width','180px');
		
			}
			else
			{
				jQuery('#cbc_' + id).css('display','block');
				jQuery('#cbi_' + id).css('display','block');
				jQuery('#cbt_' + id).css('paddingLeft', tl);
				jQuery('#cbt_' + id).css('width', tw);
			}
		}


	 function chatBoxTitelSet(id)
	 {
			//var id = "cbc";
			var img_id = "#cbImage_"+id+" img";
	       // alert("Title: "+id);
			var status = jQuery('#cbc_' + id).css("display");
			var titleLeft = jQuery(img_id).attr("width") + 10;
			if(isNaN(titleLeft) || titleLeft==10 )
			{
				titleLeft = 50;
			}
			//alert(titleLeft);
			var tl = titleLeft + "px";
			var titleWidth = 180 - titleLeft;
			var tw = titleWidth + "px";
			
			//alert(tw);
			
			if(status == "block")
			{
				jQuery('#cbt_' + id).css('paddingLeft', tl);
				jQuery('#cbt_' + id).css('width', tw);
				
				jQuery('#cbt_' + id).attr("text-align", "right");
	
			}
			else
			{
				jQuery('#cbt_' + id).css('paddingLeft','0px');
				jQuery('#cbt_' + id).css('width','180px');
				jQuery('#cbt_' + id).css("text-align", "left");

			}
	  }
	 

		function arrangeChatboxes(id)
		{
			var newFlag = true;
			var pos = 1;

	        for(var i = 0;i<chatBoxes.length; i++ )
	        {
	            arrayId = chatBoxes[i];
	            //alert("checkId: "+arrayId+" par: "+id);
	            if (arrayId  == id)
		        newFlag = false;
	        }
	        if(newFlag)
		    	chatBoxes.push(id);
		
	        for(var j = 0;j<chatBoxes.length; j++ )
	        {
	             arrayId = chatBoxes[j];
	            
	             pos = j + 1;
	             //alert("id:"+arrayId+";Pos:"+pos);
	             setPos(arrayId, pos);
	             //chatBoxTitelSet(arrayId);
	        }
		}

		function setScroll(id){
			//alert(document.getElementById(id).scrollHeight);
			//alert(document.getElementById(id).clientHeight);

			var h = document.getElementById(id).scrollHeight-document.getElementById(id).clientHeight;;

			//alert(h);
			document.getElementById(id).scrollTop= h;
			}



	//]]>