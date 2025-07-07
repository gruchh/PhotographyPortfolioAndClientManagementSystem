package com.gruchh.blog.core.config;

import com.gruchh.blog.core.entity.*;
import com.gruchh.blog.core.repository.*;
import com.gruchh.blog.security.entity.User;
import com.gruchh.blog.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PhotoShootRepository photoShootRepository;
    private final PhotoRepository photoRepository;
    private final BlogPostRepository blogPostRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeData() {
        if (userRepository.count() > 0) {
            log.info("Data already exists, skipping initialization");
            return;
        }

        try {
            log.info("Initializing sample data...");

            List<User> users = createUsers();
            userRepository.saveAll(users);

            User admin = users.get(0);
            User client1 = users.get(1);
            User client2 = users.get(2);

            List<Category> categories = createCategories();
            categoryRepository.saveAll(categories);

            Category wedding = categories.get(0);
            Category portrait = categories.get(1);
            Category landscape = categories.get(2);
            Category event = categories.get(3);
            Category family = categories.get(4);

            List<Tag> tags = createTags();
            tagRepository.saveAll(tags);

            Tag outdoor = tags.get(0);
            Tag indoor = tags.get(1);
            Tag blackWhite = tags.get(2);
            Tag vintage = tags.get(3);
            Tag nature = tags.get(4);
            Tag urban = tags.get(5);
            Tag sunset = tags.get(6);
            Tag closeup = tags.get(7);

            List<PhotoShoot> photoShoots = createPhotoShoots(client1, client2);
            photoShootRepository.saveAll(photoShoots);

            PhotoShoot weddingShoot = photoShoots.get(0);
            PhotoShoot portraitShoot = photoShoots.get(1);
            PhotoShoot familyShoot = photoShoots.get(2);

            List<Photo> photos = new ArrayList<>();
            photos.addAll(createSamplePhotos(wedding, weddingShoot, Set.of(outdoor, vintage)));
            photos.addAll(createSamplePhotos(portrait, portraitShoot, Set.of(indoor, closeup)));
            photos.addAll(createSamplePhotos(landscape, null, Set.of(outdoor, nature, sunset)));
            photos.addAll(createSamplePhotos(family, familyShoot, Set.of(outdoor, nature)));
            photos.addAll(createSamplePhotos(event, null, Set.of(indoor, urban)));
            photoRepository.saveAll(photos);

            List<BlogPost> blogPosts = createBlogPosts(admin);
            blogPostRepository.saveAll(blogPosts);

            log.info("Sample data initialized successfully!");
        } catch (Exception e) {
            log.error("Failed to initialize sample data: {}", e.getMessage(), e);
            throw new RuntimeException("Data initialization failed", e);
        }
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@add.pl");
        admin.setRoles(Set.of(Role.ADMIN, Role.USER));
        users.add(admin);

        User client1 = new User();
        client1.setUsername("client1");
        client1.setPassword(passwordEncoder.encode("client1"));
        client1.setEmail("client@cl.pl");
        client1.setRoles(Set.of(Role.USER));
        users.add(client1);

        User client2 = new User();
        client2.setUsername("client2");
        client2.setPassword(passwordEncoder.encode("client2"));
        client2.setEmail("client2@cl.pl");
        client2.setRoles(Set.of(Role.USER));
        users.add(client2);

        return users;
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(createCategory("Wesela", "Fotografia ślubna i weselna", "wesela"));
        categories.add(createCategory("Portrety", "Sesje portretowe", "portrety"));
        categories.add(createCategory("Krajobrazy", "Fotografia krajobrazowa", "krajobrazy"));
        categories.add(createCategory("Wydarzenia", "Fotografia eventowa", "wydarzenia"));
        categories.add(createCategory("Rodzinne", "Sesje rodzinne", "rodzinne"));
        return categories;
    }

    private Category createCategory(String name, String description, String slug) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setSlug(slug);
        category.setIsActive(true);
        return category;
    }

    private List<Tag> createTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(createTag("outdoor", "Na zewnątrz"));
        tags.add(createTag("indoor", "W pomieszczeniu"));
        tags.add(createTag("black-white", "Czarno-białe"));
        tags.add(createTag("vintage", "Vintage"));
        tags.add(createTag("nature", "Natura"));
        tags.add(createTag("urban", "Miejskie"));
        tags.add(createTag("sunset", "Zachód słońca"));
        tags.add(createTag("closeup", "Zbliżenie"));
        return tags;
    }

    private Tag createTag(String slug, String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setSlug(slug);
        return tag;
    }

    private List<PhotoShoot> createPhotoShoots(User client1, User client2) {
        List<PhotoShoot> photoShoots = new ArrayList<>();
        photoShoots.add(createPhotoShoot(
                "Ślub Anny i Piotra",
                "Piękna ceremonia w Krakowie z przyjęciem w plenerze",
                LocalDate.of(2024, 6, 15),
                ShootType.WEDDING,
                "Kraków, Wawel",
                client1
        ));
        photoShoots.add(createPhotoShoot(
                "Sesja biznesowa Maria",
                "Profesjonalna sesja zdjęciowa dla LinkedIn",
                LocalDate.of(2024, 7, 20),
                ShootType.PORTRAIT,
                "Studio, Warszawa",
                client2
        ));
        photoShoots.add(createPhotoShoot(
                "Sesja rodzinna Nowakowie",
                "Sesja z dziećmi w parku",
                LocalDate.of(2024, 8, 10),
                ShootType.FAMILY,
                "Park Łazienkowski, Warszawa",
                client2
        ));
        return photoShoots;
    }

    private PhotoShoot createPhotoShoot(String name, String description, LocalDate shootDate,
                                        ShootType shootType, String location, User client) {
        PhotoShoot photoShoot = new PhotoShoot();
        photoShoot.setName(name);
        photoShoot.setDescription(description);
        photoShoot.setShootDate(shootDate);
        photoShoot.setShootType(shootType);
        photoShoot.setLocation(location);
        photoShoot.setClient(client);
        photoShoot.setCreatedAt(LocalDateTime.now().minusWeeks(2));
        photoShoot.setIsActive(true);
        return photoShoot;
    }

    private List<Photo> createSamplePhotos(Category category, PhotoShoot photoShoot, Set<Tag> tags) {
        List<Photo> photos = new ArrayList<>();
        String[] photoTitles = {
                "Magiczny moment", "Uśmiech szczęścia", "Złote światło",
                "Spontaniczny kadr", "Delikatność", "Emocje w kadrze",
                "Naturalność", "Chwila zadumy", "Radosny śmiech"
        };
        String[] descriptions = {
                "Wyjątkowy moment uchwycony w idealnym świetle",
                "Naturalna radość i spontaniczność",
                "Gra światła i cienia tworzy magiczny nastrój",
                "Autentyczne emocje w najlepszym wydaniu",
                "Delikatne ujęcie pełne ciepła",
                "Moment, który zostanie w pamięci na zawsze"
        };

        for (int i = 0; i < 5; i++) {
            Photo photo = new Photo();
            photo.setTitle(photoTitles[i % photoTitles.length] + " " + (i + 1));
            photo.setDescription(descriptions[i % descriptions.length]);
            photo.setFileName("photo_" + category.getSlug() + "_" + (i + 1) + ".jpg");
            photo.setFilePath("/uploads/photos/photo_" + category.getSlug() + "_" + (i + 1) + ".jpg");
            photo.setThumbnailPath("/uploads/thumbnails/thumb_" + category.getSlug() + "_" + (i + 1) + ".jpg");
            photo.setFileSize(2500000L + (i * 100000L));
            photo.setImageWidth(1920 + (i * 100));
            photo.setImageHeight(1280 + (i * 50));
            photo.setUploadDate(LocalDateTime.now().minusDays(30 - i));
            photo.setIsPublic(i < 3);
            photo.setIsFeatured(i == 0);
            photo.setCategory(category);
            photo.setPhotoShoot(photoShoot);
            photo.setTags(new HashSet<>(tags));
            photos.add(photo);
        }
        return photos;
    }

    private List<BlogPost> createBlogPosts(User author) {
        List<BlogPost> blogPosts = new ArrayList<>();
        String[] titles = {
                "Jak przygotować się do sesji ślubnej",
                "Najlepsze lokalizacje na sesje w Warszawie",
                "Trendy w fotografii 2024",
                "Porady dla młodych par - sesja narzeczeńska"
        };
        String[] excerpts = {
                "Poznaj najważniejsze wskazówki dotyczące przygotowania do sesji ślubnej...",
                "Odkryj najpiękniejsze miejsca w stolicy idealne na sesje fotograficzne...",
                "Przegląd najnowszych trendów w fotografii ślubnej i portretowej...",
                "Praktyczne porady jak się przygotować do sesji narzeczeńskiej..."
        };
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore.";

        for (int i = 0; i < titles.length; i++) {
            BlogPost post = new BlogPost();
            post.setTitle(titles[i]);
            post.setSlug(titles[i].toLowerCase()
                    .replaceAll("ą", "a")
                    .replaceAll("ę", "e")
                    .replaceAll("ł", "l")
                    .replaceAll("ń", "n")
                    .replaceAll("ó", "o")
                    .replaceAll("ś", "s")
                    .replaceAll("ź", "z")
                    .replaceAll("ż", "z")
                    .replaceAll("ć", "c")
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "-"));
            post.setContent(content);
            post.setExcerpt(excerpts[i]);
            post.setFeaturedImage("/uploads/blog/blog_" + (i + 1) + ".jpg");
            post.setAuthor(author);
            post.setCreatedAt(LocalDateTime.now().minusDays(60 - (i * 15)));
            post.setUpdatedAt(LocalDateTime.now().minusDays(55 - (i * 15)));
            post.setPublishedAt(LocalDateTime.now().minusDays(50 - (i * 15)));
            post.setIsPublished(true);
            post.setViewCount((long) (100 + i * 25));
            blogPosts.add(post);
        }
        return blogPosts;
    }
}