import java.util.Iterator;
import java.util.NoSuchElementException;

public class MovieDB {

	private MyLinkedList<Genre> database; 

    public MovieDB() {
		database = new MyLinkedList<>();
    }

    public void insert(MovieDBItem item) {

		String itemGenre = item.getGenre();
		String itemTitle = item.getTitle();

		if (database.isEmpty()) {
			Genre newGenre = new Genre(itemGenre);
			newGenre.getList().add(itemTitle);
			database.add(newGenre);
		} else {
			MyLinkedListIterator<Genre> itr = database.iterator();
			while (itr.hasNext()) {
				Genre genreItr = itr.next();
				// find existing genre
				if (genreItr.getItem().compareTo(itemGenre) == 0) {
					MyLinkedListIterator<String> titleItr = genreItr.getList().iterator();

					// if existing titlelist is empty (after delete)
					if (genreItr.getList().isEmpty()) {
						genreItr.getList().add(itemTitle);
					} // insert title by following order
					while (titleItr.hasNext()) {
						String currentTitle = titleItr.next();
						if (currentTitle.compareTo(itemTitle) == 0) {
							return;
						}
						else if (currentTitle.compareTo(itemTitle) > 0) {
							titleItr.prevInsert(itemTitle);
							return;
						} 
						else if (!titleItr.hasNext()) {
							titleItr.currInsert(itemTitle);
							return;
						}
					}
					// insert new genre by follwing order
				} else if (genreItr.getItem().compareTo(itemGenre) > 0) {
					Genre newGenre = new Genre(itemGenre);
					newGenre.getList().add(itemTitle);
					itr.prevInsert(newGenre);
					return;
				} else if (!itr.hasNext()) {
					Genre newGenre = new Genre(itemGenre);
					newGenre.getList().add(itemTitle);
					itr.currInsert(newGenre);
					return;
				}
			} 
		}
    }

    public void delete(MovieDBItem item) {

		String itemGenre = item.getGenre();
		String itemTitle = item.getTitle();


		MyLinkedListIterator<Genre> itr = database.iterator();

		while (itr.hasNext()) {
			Genre genreItr = itr.next();
			if (genreItr.getItem().compareTo(itemGenre) == 0) {
				MyLinkedListIterator<String> titleItr = genreItr.getList().iterator();
				while (titleItr.hasNext()) {
					if (titleItr.next().compareTo(itemTitle) == 0) {
						titleItr.remove();
						return;
					}
				}
			}
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
       
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		MyLinkedListIterator<Genre> itr = database.iterator();

		while (itr.hasNext()) {
			Genre genreItr = itr.next();
			MyLinkedListIterator<String> titleItr = genreItr.getList().iterator();
			while (titleItr.hasNext()) {
				String currentTitle = titleItr.next();
				if (currentTitle.contains(term)) {
					MovieDBItem matchedItem = new MovieDBItem(genreItr.getItem(),currentTitle);
					results.add(matchedItem);
				}
			}
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
 
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		MyLinkedListIterator<Genre> itr = database.iterator();

		while (itr.hasNext()) {
			Genre genreItr = itr.next();
			MyLinkedListIterator<String> titleItr = genreItr.getList().iterator();
			while (titleItr.hasNext()) {
				MovieDBItem currentItem = new MovieDBItem(genreItr.getItem(),titleItr.next());
				results.add(currentItem);
			}
		} 
    	return results;
    }
}

class Genre extends Node<String> {

	private MyLinkedList<String> titleList;

	public Genre(String name) {
		super(name);
		this.titleList = new MyLinkedList<>();
	}

	public MyLinkedList<String> getList() {
		return this.titleList;
	}
	
}
