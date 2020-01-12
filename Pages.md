![webwxgetmsgimg](/home/zihao/fj/webwxgetmsgimg.jpg)

### Login In

* username (EditView)
* passwd(EditView)
* sign in (Button -> Main Page)
* signup (Button -> Sign Up)
* guest (Button -> Guest Page)

###  Sign Up

- username (EditView)
- passwd(EditView)
- confirm (Button)

### Guest Page

- search bar(search bar)
- return (Button -> Login)

### Main Page

* target (TextView)
* *all history (Button)

* daily real-time calories record (TextView) --dynamic warning
* daily calories availability (TextView) -- dynamic warning
* daily record food/sport (List)
  * food (Button)

  * water(Button)

  * sport (Button)

  * food recipe lookup (Button -> Food Recipe Management Page)

    in the database it will map the food name to the correct food id

    so we need to validate the food name and remove any spacing/padding

### Food Management Page

* search bar (drop down top 10 recipe)
  hyperlink text -> recipe url

### Personal Profile

<extra>

* danger food list (Scroll bar)
* return (Button -> Main Page)

### Food Management

* search bar(search bar)
  * add to history (Button -> Food History)
* history (Button -> Food History)
* return (Button -> Main Page)

### Food History

- record input(Linear Layout)
  - time (EditView)
  - food (EditView)
  - amount(EditView)
  - add (Button -> Food History)

- history (scroll bar)
- return (Button -> Food History)

### Water Management (= water history)

* record input(Linear Layout)
  * time(EditView)
  * amount (EditView)
  * add (Button -> Food Water Management)

* history (scroll bar)
* return (Button -> Main Page)

### Sport Management

- search bar(search bar)
  - add to history (Button -> Sport History)
- history (Button -> Sport History)
- return (Button -> Main Page)

### Sport History

- record input(Linear Layout)
  - time (EditView)
  - food (EditView)
  - amount(EditView)
  - add (Button -> Sport History)

- history (scroll bar)
- return (Button -> Sport History)













