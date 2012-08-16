class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        '/mobile/search'(controller:'mobile'){
            action = [ GET:'search', POST:'searchResults' ]
        }

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
