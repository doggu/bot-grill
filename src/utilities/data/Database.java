package utilities.data;

import java.util.ArrayList;

public abstract class Database<T> {
    protected abstract ArrayList<T> getList();

    public abstract T getRandom();

    public T find(String input) {
        ArrayList<T> all = findAll(input);
        return all.size()>0 ? all.get(0):null;
    }

    public abstract ArrayList<T> findAll(String input);

    protected abstract WebCache[] getOnlineResources();



    public void updateCache() {
        WebCache[] webCaches = getOnlineResources();
        Thread[] threads = new Thread[webCaches.length];

        for (int i=0; i<webCaches.length; i++) {
            final WebCache file = webCaches[i];
            Thread loader = new Thread(() -> {
                try {
                    if (!file.update())
                        throw new Error("unable to update " + file.getName());
                } catch (NullPointerException npe) {
                    //SKILL_FILES[i] = new FEHeroesCache(SKILL_URLS[i]);
                }
            });
            loader.setName(file.getName());
            threads[i] = loader;
        }

        for (Thread loader:threads)
            loader.start();

        for (Thread loader:threads) {
            try {
                loader.join(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        getList();
    }
}
